package cyyGroup.cyyArt.money.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.dao.OrderDao;
import cyyGroup.cyyArt.money.dao.UserDao;
import cyyGroup.cyyArt.money.service.OrderService;
import cyyGroup.cyyArt.until.CacheUntil;
import cyyGroup.cyyArt.until.CommonUntil;
import cyyGroup.cyyArt.until.HttpUntil;
import cyyGroup.cyyArt.vo.Express;
import cyyGroup.cyyArt.vo.Order;
import cyyGroup.cyyArt.vo.Response;
import cyyGroup.cyyArt.vo.User;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private UserDao userDao;

	@Value("${application.pic.path}")
	private String picPath;

	@Value("${application.syn.order.num}")
	private Integer synOrderNum;

	@Value("${bai.order.all.url}")
	private String orderAllUrl;

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public List<Order> getOrderList(String param) {
		Integer pay = null;
		if (!CommonUntil.isEmptyStr(param)) {
			String[] split = param.split(" ");
			for (String s : split) {
				if (s.startsWith("f")) {
					if (s.contains("未")) {
						pay = 0;
					} else if (s.contains("已")) {
						pay = 1;
					}

				}
			}
		}
		List<Order> orderList = orderDao.getOrderList(pay);

		return orderList;
	}

	@Override
	public void saveOrder(MultipartHttpServletRequest mulRequest, Order order) throws Exception {

		String file = uploadFile(mulRequest);
		order.setPicUrl(file);

		orderDao.saveOrder(order);
	}

	private String uploadFile(MultipartHttpServletRequest MulRequest)
	        throws Exception {
		Iterator<String> fileNames = MulRequest.getFileNames();
		String realPath = "";
		String m = "";
		// 遍历请求中的图片信息
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dates = format.format(date);
		while (fileNames.hasNext()) {
			// 图片对应的参数名
			String fileName = fileNames.next();
			// 获取到图片
			MultipartFile file = MulRequest.getFile(fileName);
			if (file != null) {
				String fileType = file.getContentType();
				String suffix = fileType.equals("image/jpeg") ? ".jpg"
				        : (fileType.equals("image/gif") ? ".gif" : (fileType.equals("image/png") ? ".png" : ".jpg"));
				realPath = dates + File.separator + System.currentTimeMillis() + suffix;
				File newFile = new File(picPath + File.separator + realPath);
				if (!newFile.getParentFile().exists()) {
					// 文件目录不存在，则创建它
					if (!newFile.getParentFile().mkdirs()) {
						return "";
					}
				}
				saveImg(file, picPath + File.separator + realPath);
				m += realPath + ",";
			}
		}
		if (m.length() > 1) {
			m = m.substring(0, m.length() - 1);
		}
		return m;
	}

	/**
	 * 保存文件，直接以multipartFile形式
	 * 
	 * @param multipartFile
	 * @param path
	 *            文件保存绝对路径
	 * @return 返回文件名
	 * @throws IOException
	 */
	private void saveImg(MultipartFile multipartFile, String path) throws IOException {
		FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
		byte[] bs = new byte[1024];
		int len;
		while ((len = fileInputStream.read(bs)) != -1) {
			bos.write(bs, 0, len);
		}
		bos.flush();
		bos.close();
	}

	@Override
	public MessageEnum synOrders(int belongId, Integer pageNo) throws UnsupportedEncodingException {
		String cookies = userDao.getAttribute("cookies");
		String url = orderAllUrl + "&page=" + pageNo;
		String res = HttpUntil.get(url, cookies, null);
		logger.info("synOrders url is:" + url + ";res is:" + res);
		// 如果没有结果应该是没有登录
		if (CommonUntil.isEmptyStr(res)) {
			return MessageEnum.BAI_NOTLOGIN;
		}

		JSONObject objAll = JSONObject.parseObject(res);
		if (objAll == null) {
			return MessageEnum.BAI_NOTLOGIN;
		}

		JSONObject objdata = objAll.getJSONObject("data");
		if (objdata == null) {
			return MessageEnum.BAI_NOTLOGIN;
		}

		JSONArray objlist = objdata.getJSONArray("list");
		if (objlist == null || objlist.size() == 0) {
			return MessageEnum.BAI_NOTLOGIN;
		}
		// 这里要去更新ktd——id的属性，防止变化
		updateAttrKtdId(belongId, objlist.get(0));

		Order order = null;
		String baiId = null;
		List<Order> orderList = null;

		String attrKey = null;
		String attrVal = null;
		StringBuffer sb = null;

		Integer userId = -1;
		User user = null;
		Map<String, Object> map = null;

		String dates = null;
		for (Object object : objlist) {

			// 判断有没有同步过
			baiId = ((JSONObject) object).getString("id");
			orderList = orderDao.getOrderListByBaiId(baiId);

			JSONArray objOrderItems = ((JSONObject) object).getJSONArray("order_items");
			JSONObject objOrderItemFir = objOrderItems.getJSONObject(0);

			if (!CommonUntil.isEmptyList(orderList)) {
				orderDao.updateOrderStatusByBaiId(baiId, objOrderItemFir.getInteger("status"),
				        objOrderItemFir.getString("express_url"));
			} else {

				order = new Order();
				order.setBaiId(((JSONObject) object).getString("id"));
				order.setKdt_id(((JSONObject) object).getString("kdt_id"));

				order.setStatus(objOrderItemFir.getInteger("status"));

				// 得到用户id
				map = getUserId(objOrderItemFir.getString("detail_url"), cookies);
				userId = (Integer) map.get("userId");
				if (userId == 0) {
					return MessageEnum.SYN_USER_FIR;
				}
				user = new User();
				user.setId(userId);
				order.setUser(user);

				dates = (String) map.get("createTime");
				order.setCreateTime(dates);

				order.setExpressUrl(objOrderItemFir.getString("express_url"));

				JSONArray objItems = objOrderItemFir.getJSONArray("items");
				JSONObject objItemsFir = objItems.getJSONObject(0);
				order.setName(objItemsFir.getString("title"));

				order.setPicUrl(objItemsFir.getString("image"));

				order.setNum(objItemsFir.getInteger("num"));

				JSONArray ojbSku = objItemsFir.getJSONArray("sku");
				if (ojbSku != null) {
					sb = new StringBuffer();
					for (Object obj : ojbSku) {
						attrKey = ((JSONObject) obj).getString("k");
						attrVal = ((JSONObject) obj).getString("v");

						sb.append(attrKey + ":" + attrVal);
						sb.append(";");
					}
					order.setOrderDescribe(sb.toString());
				}

				orderDao.saveOrder(order);
			}
		}
		boolean has_next = objdata.getBooleanValue("has_next");
		CacheUntil.hasSynOrderNum = CacheUntil.hasSynOrderNum + objlist.size();
		if (has_next && CacheUntil.hasSynOrderNum < synOrderNum) {
			this.synOrders(belongId, pageNo + 1);
		}
		return MessageEnum.SUCCESS;
	}

	/**
	 * 这里要去更新ktd——id的属性，防止变化
	 * 
	 * @param object
	 */
	private void updateAttrKtdId(Integer belongId, Object object) {
		String kdt_id = ((JSONObject) object).getString("kdt_id");
		userDao.updateAttr(belongId, "kdt_id", kdt_id);
	}

	/**
	 * 得到用户id和其它信息
	 * 
	 * @param string
	 * @return
	 */
	private Map<String, Object> getUserId(String url, String cookies) {
		String html = HttpUntil.get(url, cookies, null);
		String[] split = html.split("\n");

		// 拿到特定的行
		String receiverLine = null;
		for (String s : split) {
			if (s.contains("window._global")) {
				receiverLine = s;
				break;
			}
		}

		String[] split2 = receiverLine.split("window._global =");
		JSONObject objAll = JSONObject.parseObject(split2[1].trim());
		JSONObject objOrderAddressInfo = objAll.getJSONObject("orderAddressInfo");
		String name = objOrderAddressInfo.getString("receiverName");
		String tel = objOrderAddressInfo.getString("receiverTel");

		Map<String, Object> map = new HashMap<String, Object>();
		List<User> userList = userDao.getUserListByRealNameTel(name, tel);
		if (CommonUntil.isEmptyList(userList)) {
			map.put("userId", 0);
			return map;
		} else {
			map.put("userId", userList.get(0).getId());
		}

		JSONObject objOrderInfo = objAll.getJSONObject("orderInfo");
		Long createTimeL = objOrderInfo.getLong("createTime");
		Date date = new Date(createTimeL);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dates = format.format(date);
		map.put("createTime", dates);

		return map;
	}

	@Override
	public HashMap<String, Object> checkNotPay() {
		return orderDao.getNotPayCount();
	}

	@Override
	public MessageEnum changePay(Integer id, Integer prePay, String money) {
		if (prePay == 1 && CommonUntil.isEmptyStr(money)) {
			return MessageEnum.INPUTMONEY;
		} else if (prePay == 0) {
			orderDao.updateOrderPay(id, 1, money);
		} else if (prePay == 1) {
			orderDao.updateOrderPay(id, 0, money);
		}
		return MessageEnum.SUCCESS;
	}

	@Override
	public Response<Express> getExp(Integer id) {
		Response<Express> response = null;

		Order order = orderDao.getOrderListById(id);
		String expressUrl = order.getExpressUrl();
		String html = HttpUntil.get(expressUrl, null, null);
		if (CommonUntil.isEmptyStr(html)) {
			response = new Response<Express>(MessageEnum.FAIL);
			return response;
		}

		String[] split = html.split("\n");
		// 拿到特定的行
		String receiverLine = null;
		for (String s : split) {
			if (s.contains("window._global")) {
				receiverLine = s;
				break;
			}
		}

		String[] split2 = receiverLine.split("window._global =");
		JSONObject objAll = JSONObject.parseObject(split2[1]);
		JSONArray objDeliveryOrder = objAll.getJSONArray("deliveryOrder");
		JSONObject objExpress = objDeliveryOrder.getJSONObject(0).getJSONObject("express");
		Express express = JSONObject.parseObject(objExpress.toString(), Express.class);
		response = new Response<Express>(MessageEnum.SUCCESS);
		response.setData(express);

		return response;
	}

	@Override
	public void keepAlive() {
		String cookies = userDao.getAttribute("cookies");
		String url = orderAllUrl + "&page=1";
		String res = HttpUntil.get(url, cookies, null);
		logger.info("keepAlive url is:" + url + ",res is:" + res);
	}

	@Override
	public MessageEnum checkBaiLogin() {
		String cookies = userDao.getAttribute("cookies");
		String url = orderAllUrl + "&page=1";
		String res = HttpUntil.get(url, cookies, null);

		// 如果没有结果应该是没有登录
		if (CommonUntil.isEmptyStr(res)) {
			return MessageEnum.BAI_NOTLOGIN;
		}

		JSONObject objAll = JSONObject.parseObject(res);
		if (objAll == null) {
			return MessageEnum.BAI_NOTLOGIN;
		}

		JSONObject objdata = objAll.getJSONObject("data");
		if (objdata == null) {
			return MessageEnum.BAI_NOTLOGIN;
		}

		JSONArray objlist = objdata.getJSONArray("list");
		if (objlist == null || objlist.size() == 0) {
			return MessageEnum.BAI_NOTLOGIN;
		}
		return MessageEnum.SUCCESS;
	}
}
