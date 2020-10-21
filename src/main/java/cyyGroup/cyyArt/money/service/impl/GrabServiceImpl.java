package cyyGroup.cyyArt.money.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.dao.GrabDao;
import cyyGroup.cyyArt.money.dao.UserDao;
import cyyGroup.cyyArt.money.service.GrabService;
import cyyGroup.cyyArt.order.vo.DiscountGood;
import cyyGroup.cyyArt.order.vo.NewSubmitAll;
import cyyGroup.cyyArt.order.vo.NewSubmitGood;
import cyyGroup.cyyArt.order.vo.NewSubmitUtil;
import cyyGroup.cyyArt.order.vo.SubmitActivitie;
import cyyGroup.cyyArt.order.vo.SubmitAddress;
import cyyGroup.cyyArt.order.vo.SubmitConfig;
import cyyGroup.cyyArt.order.vo.SubmitDelivery;
import cyyGroup.cyyArt.order.vo.SubmitItem;
import cyyGroup.cyyArt.order.vo.SubmitItemSource;
import cyyGroup.cyyArt.order.vo.SubmitOrder;
import cyyGroup.cyyArt.order.vo.SubmitOrderAuto;
import cyyGroup.cyyArt.order.vo.SubmitSeller;
import cyyGroup.cyyArt.order.vo.SubmitSource;
import cyyGroup.cyyArt.order.vo.SubmitUmp;
import cyyGroup.cyyArt.order.vo.SubmitUseCustomerCardInfo;
import cyyGroup.cyyArt.task.GrabTask;
import cyyGroup.cyyArt.until.CacheUntil;
import cyyGroup.cyyArt.until.CommonUntil;
import cyyGroup.cyyArt.until.HttpUntil;
import cyyGroup.cyyArt.vo.ShoppingCart;
import cyyGroup.cyyArt.vo.User;

@Service
public class GrabServiceImpl implements GrabService {

	@Autowired
	private GrabDao grabDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private GrabTask grabTask;

	@Value("${bai.new.url}")
	private String baiNewUrl;

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public List<ShoppingCart> getGrabList(Integer fromType, Integer userId) {
		List<Integer> fromTypes = new ArrayList<Integer>();
		if (fromType == 1) {
			fromTypes.add(1);
			fromTypes.add(3);
		} else if (fromType == 2) {
			fromTypes.add(2);
		}
		return grabDao.getGrabList(fromTypes, userId);
	}

	@Override
	public List<ShoppingCart> getCartlist() {
		// 接口请求
		String cookies = userDao.getAttribute("cookies");
		String kdt_id = userDao.getAttribute("kdt_id");

		JSONObject objAll = getCartObjAll(kdt_id, cookies);

		JSONArray objCartList = objAll.getJSONArray("cart_list");
		if (objCartList == null || objCartList.size() == 0) {
			return new ArrayList<ShoppingCart>();
		}

		JSONObject objCartListFir = objCartList.getJSONObject(0);
		JSONArray objItems = objCartListFir.getJSONArray("items");

		JSONArray objUnitems = objCartListFir.getJSONArray("unavailable_items");

		List<ShoppingCart> cartList = JSONObject.parseArray(objItems.toJSONString(), ShoppingCart.class);
		if (objUnitems != null && objUnitems.size() > 0) {
			List<ShoppingCart> cartUnList = JSONObject.parseArray(objUnitems.toJSONString(), ShoppingCart.class);
			cartList.addAll(cartUnList);
		}

		// 名字设置好
		setTitle(cartList);
		// 名字代理购买人
		setBuyer(cartList, objAll.getInteger("buyer_id"));

		return cartList;
	}

	/**
	 * 得到购物车信息
	 * 
	 * @return
	 */
	private JSONObject getCartObjAll(String kdt_id, String cookies) {
		long currentTimeMillis = System.currentTimeMillis();
		String url = "https://h5.youzan.com/wsctrade/cart?kdt_id=" + kdt_id + "&reft=" + currentTimeMillis + "&spm=uc."
		        + kdt_id;
		String html = HttpUntil.get(url, cookies, null);
		logger.info("getCartObjAll,url:" + url + ",res:" + html);

		String[] split = html.split("\n");

		// 拿到特定的行
		String adressLine = null;
		for (String s : split) {
			if (s.contains("window._global")) {
				adressLine = s;
				break;
			}
		}

		String[] split2 = adressLine.split("window._global =");
		JSONObject objAll = JSONObject.parseObject(split2[1].trim());
		return objAll;
	}

	private void setBuyer(List<ShoppingCart> cartList, Integer buyer_id) {
		for (ShoppingCart shoppingCart : cartList) {
			shoppingCart.setBuyer_id(buyer_id);
		}

	}

	/**
	 * 名字设置好
	 * 
	 * @param cartList
	 */
	private void setTitle(List<ShoppingCart> cartList) {

		String sku = null;
		JSONArray skuArr = null;
		StringBuffer sb = null;
		for (ShoppingCart shoppingCart : cartList) {
			sku = shoppingCart.getSku();
			if (CommonUntil.isEmptyStr(sku)) {
				continue;
			}

			skuArr = JSONObject.parseArray(sku);
			if (skuArr == null || skuArr.size() == 0) {
				continue;
			}

			sb = new StringBuffer();
			for (Object object : skuArr) {
				sb.append(((JSONObject) object).getString("k"));
				sb.append(":");
				sb.append(((JSONObject) object).getString("v"));
				sb.append(";");
			}
			shoppingCart.setSku(sb.toString());
		}

	}

	@Override
	public MessageEnum addGrab(Integer time1, Integer time2, Integer num, Integer userId, String alias,
	        String remark) {
		// 拿到ShoppingCart
		ShoppingCart shoppingCart = getShoppingCart(alias);
		if (shoppingCart == null) {
			return MessageEnum.FAIL;
		}
		User user = new User();
		user.setId(userId);

		shoppingCart.setUser(user);
		shoppingCart.setNum(num);

		SimpleDateFormat formatShort = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateS = formatShort.format(date);

		String hour = time1 + "";
		if (time1 < 10) {
			hour = "0" + time1;
		}
		String minte = time2 + "";
		if (time2 < 10) {
			minte = "0" + time2;
		}
		String second = "00";

		dateS = dateS + " " + hour + ":" + minte + ":" + second;
		shoppingCart.setGrabTime(dateS);

		grabDao.saveShoppingCart(shoppingCart, remark);

		return MessageEnum.SUCCESS;
	}

	private ShoppingCart getShoppingCart(String alias) {
		for (ShoppingCart shoppingCart : CacheUntil.tempCartList) {
			if (alias.equals(shoppingCart.getAlias())) {
				return shoppingCart;
			}
		}
		return null;
	}

	@Override
	public synchronized void prepareGrab() throws Exception {
		List<ShoppingCart> shoppingCartList = grabDao.getGrabListByStatus(1);
		if (CommonUntil.isEmptyList(shoppingCartList)) {
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		int fromType = 0;

		List<ShoppingCart> newGrabList = new ArrayList<ShoppingCart>();
		List<ShoppingCart> grabAutoList = new ArrayList<ShoppingCart>();

		for (ShoppingCart shoppingCart : shoppingCartList) {

			date = format.parse(shoppingCart.getGrabTime());
			long timeCur = System.currentTimeMillis();
			long timeGrab = date.getTime();
			long diff = timeGrab - timeCur;
			logger.info(timeCur + ":" + timeGrab + ":" + diff);
			if (diff > 10000L) {
				continue;
			}

			fromType = shoppingCart.getFromType();
			if (fromType == 1) {
				// 有赞平台
				prepareGrabYouzan(shoppingCart, date.getTime());
			} else if (fromType == 2) {
				// 新平台
				newGrabList.add(shoppingCart);
			} else if (fromType == 3) {
				// 有赞。全自动
				grabAutoList.add(shoppingCart);
			}

		}

		if (newGrabList.size() > 0) {
			prepareGrabNew(newGrabList);
		}

		if (grabAutoList.size() > 0) {
			prepareGrabAuto(grabAutoList);
		}
	}

	/**
	 * 有赞。全自动
	 * 
	 * @param newGrabList
	 * @throws Exception
	 */
	private void prepareGrabAuto(List<ShoppingCart> newGrabList) throws Exception {
		List<SubmitOrderAuto> autoAllList = new ArrayList<SubmitOrderAuto>();
		SubmitOrderAuto orderAuto = null;
		for (ShoppingCart cart : newGrabList) {
			orderAuto = new SubmitOrderAuto();
			orderAuto.setShoppingCart(cart);

			autoAllList.add(orderAuto);
		}

		// 取出上线的
		String cookies = userDao.getAttribute("cookies");
		List<SubmitOrderAuto> autoOnsaleList = checkGoodSaleAuto(autoAllList, cookies);
		if (CommonUntil.isEmptyList(autoOnsaleList)) {
			return;
		}

		// 取出目的
		List<SubmitOrderAuto> autoDesList = takeGoodIdAuto(autoOnsaleList, cookies);
		if (CommonUntil.isEmptyList(autoDesList)) {
			return;
		}

		// 取出sessionId
		// 接口请求
		String kdt_id = userDao.getAttribute("kdt_id");
		JSONObject objAll = getCartObjAll(kdt_id, cookies);

		JSONArray objCartList = objAll.getJSONArray("cart_list");
		if (objCartList == null || objCartList.size() == 0) {
			return;
		}

		JSONObject objCartListFir = objCartList.getJSONObject(0);
		JSONArray objItems = objCartListFir.getJSONArray("items");
		String sessionId = null;
		if (objItems == null || objItems.size() == 0) {
			JSONArray objUnitems = objCartListFir.getJSONArray("unavailable_items");
			if (objUnitems != null && objUnitems.size() > 0) {
				sessionId = objUnitems.getJSONObject(0).getString("nobody");
			}
		} else {
			sessionId = objItems.getJSONObject(0).getString("nobody");
		}
		if (sessionId == null) {
			return;
		}

		ShoppingCart shoppingCart = null;
		for (SubmitOrderAuto auto : autoDesList) {
			shoppingCart = auto.getShoppingCart();
			shoppingCart.setNobody(sessionId);
			shoppingCart.setKdt_id(Integer.valueOf(kdt_id));
			shoppingCart.setBuyer_id(objAll.getInteger("buyer_id"));
			shoppingCart.setGoods_id(auto.getGoodId());
			shoppingCart.setSku_id(auto.getSku_id());
			shoppingCart.setCreated_time(0L);
			shoppingCart.setUpdated_time(0L);

			prepareGrabYouzan(shoppingCart, auto.getStartSoldTime());
		}

	}

	private List<SubmitOrderAuto> takeGoodIdAuto(List<SubmitOrderAuto> autoOnsaleList, String cookies) {

		List<SubmitOrderAuto> desList = new ArrayList<SubmitOrderAuto>();

		String sku = null;
		String[] skuArray = null;
		String[] skuNeedArray = null;
		String[] skuNotArray = null;

		Integer goodId = null;
		String url = null;

		// goodid对应特征集合
		Map<Integer, Map<Integer, String>> spectListCacheMap = new HashMap<Integer, Map<Integer, String>>();
		// goodid对应价格集合
		// Map<Integer, Map<Integer, Integer>> priceListCacheMap = new HashMap<Integer,
		// Map<Integer, Integer>>();

		// sku对应单个skuId
		Map<String, Integer> spectCacheMap = new HashMap<String, Integer>();

		// 单个特征集合
		Map<Integer, String> specMapCacheMap = null;
		// 单个skuId
		Integer skuId = null;
		// 单个price
		// Integer price = null;

		for (SubmitOrderAuto order : autoOnsaleList) {

			// 解析需求--规格
			sku = order.getShoppingCart().getSku();
			skuArray = null;
			skuNeedArray = null;
			skuNotArray = null;
			if (!CommonUntil.isEmptyStr(sku)) {
				skuArray = sku.split("\\*");
				skuNeedArray = skuArray[0].split("#");
				if (skuArray.length > 1) {
					skuNotArray = skuArray[1].split("#");
				}
			}

			goodId = order.getGoodId();

			specMapCacheMap = spectListCacheMap.get(goodId);
			if (specMapCacheMap != null) {

				skuId = spectCacheMap.get("" + goodId + sku);
				if (skuId == null) {
					skuId = handUtilDesAuto(skuNeedArray, skuNotArray, specMapCacheMap);
				}

				if (skuId != null) {
					order.setSku_id(skuId);

					// price = priceListCacheMap.get(goodId).get(skuId);
					// order.setPrice(price);

					desList.add(order);

					spectCacheMap.put("" + goodId + sku, skuId);
				}
				continue;
			}

			// 取出详情
			url = order.getUrl();
			String goodDetailRes = HttpUntil.get(url, cookies, null);
			logger.info("takeGoodIdAuto,url:" + url + ",res:" + goodDetailRes);

			if (goodDetailRes == null) {
				continue;
			}

			String[] split = goodDetailRes.split("\n");

			// 拿到特定的行
			String desLine = null;
			for (String s : split) {
				if (s.contains("window._global")) {
					desLine = s;
					break;
				}
			}

			String[] split2 = desLine.split("window._global =");
			JSONObject objAll = JSONObject.parseObject(split2[1].trim());

			JSONObject objGoodsData = objAll.getJSONObject("goodsData");
			if (objGoodsData == null) {
				continue;
			}

			// 去构造特征的map
			Map<Integer, String> specMap = getSpecMap(objGoodsData);
			if (specMap == null || specMap.size() == 0) {
				continue;
			}
			// 缓存特征
			spectListCacheMap.put(goodId, specMap);

			// 去构造价格的map
			// Map<Integer, Integer> priceMap = getPriceMap(objGoodsData);
			// if (priceMap == null || priceMap.size() == 0) {
			// continue;
			// }
			// 缓存价格
			// priceListCacheMap.put(goodId, priceMap);

			// 拿出目的id
			skuId = handUtilDesAuto(skuNeedArray, skuNotArray, specMap);
			if (skuId != null) {
				order.setSku_id(skuId);
				// order.setPrice(priceMap.get(skuId));

				desList.add(order);

				spectCacheMap.put("" + goodId + sku, skuId);
			}

		}
		return desList;
	}

	/**
	 * 去构造价格的map
	 * 
	 * @param objGoodsData
	 * @return
	 */
	private Map<Integer, Integer> getPriceMap(JSONObject objGoodsData) {
		Map<Integer, Integer> priceMap = new HashMap<Integer, Integer>();

		JSONObject objSkuInfo = objGoodsData.getJSONObject("skuInfo");
		JSONArray objpriceList = objSkuInfo.getJSONArray("skuPrices");

		JSONObject objprice = null;
		for (Object object : objpriceList) {
			objprice = (JSONObject) object;
			priceMap.put(objprice.getInteger("skuId"), objprice.getInteger("price"));
		}
		return priceMap;
	}

	/**
	 * 拿出目的id --有赞
	 * 
	 * @param skuArray
	 * @param skuNotArray
	 * @param specMap
	 * @return
	 */
	private Integer handUtilDesAuto(String[] skuNeedArray, String[] skuNotArray, Map<Integer, String> specMap) {
		// 没有规格的情况
		if (specMap.size() == 1) {
			// 不管有没有填，就返回这一个
			for (Entry<Integer, String> map : specMap.entrySet()) {
				return map.getKey();
			}
		} else {
			// 有规格的情况
			// 没有填规格
			if (skuNeedArray == null) {
				return null;
			} else {
				// 填
				String specAll = null;
				boolean isDesId = false;
				for (Entry<Integer, String> map : specMap.entrySet()) {

					isDesId = false;
					specAll = map.getValue();
					isDesId = checkDesGood(specAll, skuNeedArray, skuNotArray);
					if (isDesId) {
						return map.getKey();
					}
				}
			}
		}
		return null;
	}

	private boolean checkDesGood(String specAll, String[] skuNeedArray, String[] skuNotArray) {
		boolean isDesGood = true;
		specAll = specAll.toUpperCase();
		for (String sku : skuNeedArray) {
			sku = sku.toUpperCase();
			if (!specAll.contains(sku)) {
				isDesGood = false;
				break;
			}
		}

		if (isDesGood && skuNotArray != null) {
			for (String sku : skuNotArray) {
				sku = sku.toUpperCase();
				if (specAll.contains(sku)) {
					isDesGood = false;
					break;
				}
			}
		}

		return isDesGood;
	}

	/**
	 * 去构造特征的map
	 * 
	 * @param objGoodsData
	 * @return
	 */
	private Map<Integer, String> getSpecMap(JSONObject objGoodsData) {

		Map<Integer, String> specMap = new HashMap<Integer, String>();

		JSONObject objSkuInfo = objGoodsData.getJSONObject("skuInfo");
		JSONArray objProps = objSkuInfo.getJSONArray("props");

		JSONObject objProp = null;
		JSONArray objPropV = null;
		JSONObject objPropVdetail = null;
		// 把所有特征用map存起来
		Map<Integer, String> specPartMap = new HashMap<Integer, String>();
		for (Object object : objProps) {
			objProp = (JSONObject) object;
			objPropV = objProp.getJSONArray("v");
			for (Object objectV : objPropV) {
				objPropVdetail = (JSONObject) objectV;
				specPartMap.put(objPropVdetail.getInteger("id"), objPropVdetail.getString("name"));
			}
		}

		JSONArray objSkus = objSkuInfo.getJSONArray("skus");
		JSONObject objSkudetail = null;
		String skuSpeBase = "s";
		Integer specId = null;
		String specDes = null;
		// 放入最张的map
		if (specPartMap.size() > 0) {
			for (Object object : objSkus) {

				specDes = "";
				objSkudetail = (JSONObject) object;
				for (int i = 1; i < 6; i++) {
					specId = objSkudetail.getInteger(skuSpeBase + i);
					if (specId == 0) {
						break;
					} else {
						specDes = specDes + specPartMap.get(specId);
					}
				}

				specMap.put(objSkudetail.getInteger("skuId"), specDes);
			}
		} else {
			JSONObject objSpuStock = objSkuInfo.getJSONObject("spuStock");
			specMap.put(objSpuStock.getInteger("skuId"), "");
		}

		return specMap;
	}

	/**
	 * 取出上线的 有赞
	 * 
	 * @param autoAllList
	 * @return
	 */
	private List<SubmitOrderAuto> checkGoodSaleAuto(List<SubmitOrderAuto> autoAllList, String cookies) {

		// 查询上线产品
		String onSaleUrl = "https://shop42728454.youzan.com/wscshop/showcase/goods/allGoods.json?pageSize=20&page=1&offlineId=0&order=&json=1&order_by=createdTime&goodsType=2";
		String onSaleRes = HttpUntil.get(onSaleUrl, cookies, null);
		if (onSaleRes == null) {
			return null;
		}

		JSONObject objAll = JSONObject.parseObject(onSaleRes);
		if (objAll == null) {
			return null;
		}

		JSONObject objData = objAll.getJSONObject("data");
		if (objData == null) {
			return null;
		}

		JSONArray objList = objData.getJSONArray("list");
		if (objList == null || objList.size() < 1) {
			return null;
		}

		String title = null;
		String[] titleArray = null;
		Integer mainGoodId = null;
		Map<String, Integer> cacheMap = new HashMap<String, Integer>();
		Map<String, Long> cacheStartMap = new HashMap<String, Long>();
		Map<String, String> cacheUrlMap = new HashMap<String, String>();

		List<SubmitOrderAuto> autoOnsaleList = new ArrayList<SubmitOrderAuto>();

		for (SubmitOrderAuto orderAuto : autoAllList) {
			// 解析需求--商品名
			title = orderAuto.getShoppingCart().getTitle();
			titleArray = title.split("#");

			mainGoodId = cacheMap.get(title);
			if (mainGoodId != null) {
				orderAuto.setGoodId(mainGoodId);
				orderAuto.setStartSoldTime(cacheStartMap.get(title));
				orderAuto.setUrl(cacheUrlMap.get(title));

				autoOnsaleList.add(orderAuto);
				continue;
			}

			JSONObject objGood = null;
			String goodName = null;
			Long startSoldTime = null;
			boolean isDesGood = true;
			for (Object object : objList) {
				objGood = (JSONObject) object;
				goodName = objGood.getString("title");

				isDesGood = true;
				for (String name : titleArray) {
					if (!goodName.contains(name)) {
						isDesGood = false;
						break;
					}
				}

				if (isDesGood) {
					mainGoodId = objGood.getInteger("id");
					cacheMap.put(title, mainGoodId);

					startSoldTime = objGood.getLong("startSoldTime");
					startSoldTime = startSoldTime * 1000L - 10000L;
					cacheStartMap.put(title, startSoldTime);

					cacheUrlMap.put(title, objGood.getString("url"));

					orderAuto.setGoodId(mainGoodId);
					orderAuto.setStartSoldTime(startSoldTime);
					orderAuto.setUrl(cacheUrlMap.get(title));

					autoOnsaleList.add(orderAuto);
					break;
				}
			}

		}
		return autoOnsaleList;
	}

	/**
	 * 新平台
	 * 
	 * @param shoppingCart
	 * @param date
	 * @throws CloneNotSupportedException
	 */
	private synchronized void prepareGrabNew(List<ShoppingCart> newGrabList) throws CloneNotSupportedException {
		// 填充工具类
		List<NewSubmitUtil> utilAll = new ArrayList<NewSubmitUtil>();
		NewSubmitUtil newSubmitUtil = null;
		for (ShoppingCart shoppingCart : newGrabList) {
			newSubmitUtil = new NewSubmitUtil();

			newSubmitUtil.setShoppingCart(shoppingCart);

			utilAll.add(newSubmitUtil);
		}

		// 取出上线的
		List<NewSubmitUtil> utilOnSale = checkGoodSale(utilAll);
		if (CommonUntil.isEmptyList(utilOnSale)) {
			return;
		}

		// 去拿分id
		List<NewSubmitUtil> utilDes = takeGoodId(utilOnSale);
		if (CommonUntil.isEmptyList(utilDes)) {
			return;
		}

		// 一切就绪后构造参数
		addPrepareList(utilDes);

	}

	/**
	 * 去拿分id
	 * 
	 * @param skuArray
	 * @return
	 */
	private synchronized List<NewSubmitUtil> takeGoodId(List<NewSubmitUtil> utilOnSale) {

		String mainGoodId = null;
		Map<String, JSONArray> spectListCacheMap = new HashMap<String, JSONArray>();
		Map<String, String> spectCacheMap = new HashMap<String, String>();
		JSONArray spectListCache = null;

		String sku = null;
		String[] skuArray = null;
		String[] skuNeedArray = null;
		String[] skuNotArray = null;

		List<NewSubmitUtil> utilDes = new ArrayList<NewSubmitUtil>();
		String goodIdDes = null;
		for (NewSubmitUtil util : utilOnSale) {

			// 解析需求--规格
			sku = util.getShoppingCart().getSku();
			skuArray = null;
			skuNeedArray = null;
			skuNotArray = null;
			if (!CommonUntil.isEmptyStr(sku)) {
				skuArray = sku.split("\\*");
				skuNeedArray = skuArray[0].split("#");
				if (skuNeedArray.length == 1) {
					skuNeedArray = skuArray[0].split("＃");
				}

				if (skuArray.length > 1) {
					skuNotArray = skuArray[1].split("#");
				}
			}

			mainGoodId = util.getMainGoodId();
			spectListCache = spectListCacheMap.get(mainGoodId);
			if (spectListCache != null) {

				goodIdDes = spectCacheMap.get(mainGoodId + sku);
				if (goodIdDes == null) {
					goodIdDes = handUtilDes(spectListCache, skuNeedArray, skuNotArray);
				}

				if (goodIdDes != null) {
					String[] split = goodIdDes.split(":");

					util.setGoodId(split[0]);
					util.setPrice(split[1]);
					util.setSpec1("_self-".equals(split[2]) ? "" : split[2]);

					utilDes.add(util);

					spectCacheMap.put(mainGoodId + sku, goodIdDes);
				}
				continue;
			}

			String url = baiNewUrl + "/backend/goods/" + mainGoodId + "/specs";
			String goodListRes = HttpUntil.get(url, null, null);
			logger.info("takeGoodId,url:" + url + ",res:" + goodListRes);

			if (goodListRes == null) {
				continue;
			}

			JSONObject objAll = JSONObject.parseObject(goodListRes);
			if (objAll == null) {
				continue;
			}

			JSONObject objData = objAll.getJSONObject("data");
			if (objData == null) {
				continue;
			}

			// 没有任何规格说明数据不对
			JSONArray objArrayGoodsList = objData.getJSONArray("goodsList");
			if (objArrayGoodsList == null || objArrayGoodsList.size() < 1) {
				continue;
			}

			// 存入缓存
			spectListCacheMap.put(mainGoodId, objArrayGoodsList);
			goodIdDes = handUtilDes(objArrayGoodsList, skuNeedArray, skuNotArray);

			if (goodIdDes != null) {
				String[] split = goodIdDes.split(":");

				util.setGoodId(split[0]);
				util.setPrice(split[1]);
				util.setSpec1("_self-".equals(split[2]) ? "" : split[2]);

				utilDes.add(util);

				spectCacheMap.put(mainGoodId + sku, goodIdDes);
			}
		}

		return utilDes;
	}

	/**
	 * 取出目的id
	 * 
	 * @param spectListCache
	 * @param skuArray
	 * @param skuNotArray
	 * @param spectCacheMap
	 * @return
	 */
	private synchronized String handUtilDes(JSONArray objArrayGoodsList, String[] skuNeedArray, String[] skuNotArray) {
		JSONObject objGood = null;
		// 没有规格的情况
		if (objArrayGoodsList.size() == 1) {
			// 不管有没有填，下单唯一的一个
			objGood = objArrayGoodsList.getJSONObject(0);
			String desGoodId = objGood.getString("goodsId");
			String price = objGood.getString("price");
			return desGoodId + ":" + price + ":_self-";
		} else {
			// 有规格的情况
			// 没有填规格
			if (skuNeedArray == null) {
				return null;
			} else {
				// 填
				String specAll = null;
				String desGoodId = null;
				for (Object object : objArrayGoodsList) {
					objGood = (JSONObject) object;
					if (objGood == null) {
						continue;
					}

					specAll = getSpectAll(objGood);
					desGoodId = checkDesGood(specAll, skuNeedArray, skuNotArray, objGood);
					if (desGoodId != null) {
						String price = objGood.getString("price");
						return desGoodId + ":" + price + ":" + specAll;
					}

				}
			}
		}
		return null;
	}

	private synchronized String checkDesGood(String specAll, String[] skuNeedArray, String[] skuNotArray,
	        JSONObject objGood) {
		boolean isDesGood = true;
		specAll = specAll.toUpperCase();
		for (String sku : skuNeedArray) {
			sku = sku.toUpperCase();
			if (!specAll.contains(sku)) {
				isDesGood = false;
				break;
			}
		}

		if (isDesGood && skuNotArray != null) {
			for (String sku : skuNotArray) {
				sku = sku.toUpperCase();
				if (specAll.contains(sku)) {
					isDesGood = false;
					break;
				}
			}
		}

		if (isDesGood) {
			return objGood.getString("goodsId");
		}

		return null;
	}

	/**
	 * 动态得到特征点
	 * 
	 * @param objGood
	 * @return
	 */
	private String getSpectAll(JSONObject objGood) {
		String spec = "spec";
		String specAll = "";
		String specPart = null;
		for (int i = 1; i < 10; i++) {
			specPart = objGood.getString(spec + i);
			if (specPart == null) {
				break;
			} else {
				specAll = specAll + specPart;
			}
		}
		return specAll;
	}

	/**
	 * 判断商品有没有上线
	 * 
	 * @return
	 */
	private synchronized List<NewSubmitUtil> checkGoodSale(List<NewSubmitUtil> utilAll) {

		JSONArray objArrayRows = getObjArrayRows();

		Map<String, String> cacheMap = new HashMap<String, String>();
		Map<String, Long> cacheTimeMap = new HashMap<String, Long>();
		Map<String, String> cacheNameMap = new HashMap<String, String>();
		Map<String, String> cachePicMap = new HashMap<String, String>();

		String title = null;
		String[] titleArray = null;
		String mainGoodId = null;
		Long startSaleTime = null;
		List<NewSubmitUtil> utilMainGood = new ArrayList<NewSubmitUtil>();

		for (NewSubmitUtil util : utilAll) {
			// 解析需求--商品名
			title = util.getShoppingCart().getTitle();
			titleArray = title.split("#");

			mainGoodId = cacheMap.get(title);
			if (mainGoodId != null) {
				util.setMainGoodId(mainGoodId);
				util.setStartSaleTime(cacheTimeMap.get(title));
				util.setName(cacheNameMap.get(title));
				util.setImage(cachePicMap.get(title));

				utilMainGood.add(util);
				continue;
			}

			JSONObject objGood = null;
			String goodName = null;
			String pic = null;
			boolean isDesGood = true;
			for (Object object : objArrayRows) {
				objGood = (JSONObject) object;
				goodName = objGood.getString("name");

				isDesGood = true;
				for (String name : titleArray) {
					if (!goodName.contains(name)) {
						isDesGood = false;
						break;
					}
				}

				if (isDesGood) {
					mainGoodId = objGood.getString("goodsId");
					cacheMap.put(title, mainGoodId);
					util.setMainGoodId(mainGoodId);

					startSaleTime = objGood.getLong("shiftedOn");
					startSaleTime = startSaleTime - 10000L;
					cacheTimeMap.put(title, startSaleTime);
					util.setStartSaleTime(startSaleTime);

					cacheNameMap.put(title, goodName);
					util.setName(goodName);

					pic = objGood.getString("pic");
					cachePicMap.put(title, pic);
					util.setImage(pic);

					utilMainGood.add(util);
					break;
				}
			}
		}

		return utilMainGood;
	}

	private JSONArray getObjArrayRows() {
		// 今日上新
		String url = baiNewUrl +
		        "/backend/goods?pageIndex=1&pageSize=20&homeType=1&sortType=AES";
		String goodListRes = HttpUntil.get(url, null, null);
		logger.info("checkGoodSale today,url:" + url + ",res:" + goodListRes);

		JSONObject objAll = null;
		JSONObject objData = null;
		JSONArray objArrayRows = null;
		// 如果一个都没有说明失败
		if (goodListRes != null) {
			objAll = JSONObject.parseObject(goodListRes);
			objData = objAll.getJSONObject("data");
			if (objData != null) {
				objArrayRows = objData.getJSONArray("rows");
				if (objArrayRows != null && objArrayRows.size() > 0) {
					return objArrayRows;
				}
			}
		}

		// 综合 4:综合 2:新品首发
		url = baiNewUrl + "/backend/goods?homeType=2&goodCategory=&otherType=&pageIndex=1&pageSize=20";
		goodListRes = HttpUntil.get(url, null, null);

		// 如果一个都没有说明失败
		if (goodListRes != null) {
			objAll = JSONObject.parseObject(goodListRes);
			objData = objAll.getJSONObject("data");
			if (objData != null) {
				objArrayRows = objData.getJSONArray("rows");
				if (objArrayRows != null && objArrayRows.size() > 0) {
					return objArrayRows;
				}
			}
		}

		return null;
	}

	private void addPrepareList(List<NewSubmitUtil> utilDes) throws CloneNotSupportedException {

		NewSubmitAll submitAll = null;
		Integer userId = null;
		NewSubmitGood submitGood = null;
		NewSubmitGood discountSubmitGood = null;
		List<NewSubmitGood> submitGoodList = null;
		ShoppingCart shoppingCart = null;

		List<DiscountGood> discountGoodsList = null;
		List<NewSubmitGood> discountSubmitGoodList = null;
		DiscountGood discountGood = null;

		for (NewSubmitUtil util : utilDes) {
			submitAll = new NewSubmitAll();

			shoppingCart = util.getShoppingCart();

			userId = shoppingCart.getUser().getId();
			User user = userDao.getUserById(userId);

			submitAll.setArea(user.getCounty());
			submitAll.setCity(user.getCity());
			submitAll.setHouseNumber(user.getAddressDetail());
			submitAll.setPrice(util.getPrice());
			submitAll.setProvince(user.getProvince());
			submitAll.setReceiverName(user.getRealName());
			submitAll.setReceiverPhone(user.getTel());

			// discount相关
			discountSubmitGood = new NewSubmitGood();
			discountSubmitGood.setCount(String.valueOf(shoppingCart.getNum()));
			discountSubmitGood.setGoodId(util.getGoodId());
			discountSubmitGood.setName(util.getName());
			discountSubmitGood.setSpec1(util.getSpec1());
			discountGoodsList = new ArrayList<DiscountGood>();
			discountSubmitGoodList = new ArrayList<NewSubmitGood>();
			discountSubmitGoodList.add(discountSubmitGood);
			discountGood = new DiscountGood();
			discountGood.setGoodsList(discountSubmitGoodList);

			discountGoodsList.add(discountGood);
			submitAll.setDiscountGoodsList(discountGoodsList);

			// goodList相关
			submitGood = (NewSubmitGood) discountSubmitGood.clone();
			submitGood.setImage(util.getImage());

			submitGoodList = new ArrayList<NewSubmitGood>();
			submitGoodList.add(submitGood);

			submitAll.setGoodsList(submitGoodList);

			String submitOrder_s = JSONObject.toJSONString(submitAll);
			logger.info(submitOrder_s);

			grabDao.updateStatusById(shoppingCart.getId(), 2, null, shoppingCart.getRemark());

			logger.info("准备抢单:" + shoppingCart.getTitle());

			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("ojb", submitOrder_s);

			taskMap.put("time", util.getStartSaleTime() == null ? 0L : util.getStartSaleTime());
			taskMap.put("id", shoppingCart.getId());
			taskMap.put("remark", shoppingCart.getRemark());

			grabTask.executor(4, taskMap);
		}
	}

	/**
	 * 有赞平台
	 * 
	 * @param shoppingCart
	 * @param date
	 * @throws Exception
	 */
	private void prepareGrabYouzan(ShoppingCart shoppingCart, Long grabTime) throws Exception {

		SubmitOrder submitOrder = new SubmitOrder();
		SubmitConfig submitConfig = new SubmitConfig();
		submitOrder.setConfig(submitConfig);

		Integer userId = shoppingCart.getUser().getId();
		User user = userDao.getUserById(userId);
		SubmitDelivery submitDelivery = getSubmitDelivery(shoppingCart, user);
		submitOrder.setDelivery(submitDelivery);

		List<SubmitItem> submitItems = getSubmitItems(shoppingCart);
		submitOrder.setItems(submitItems);

		SubmitSeller submitSeller = getSubmitSeller(shoppingCart);
		submitOrder.setSeller(submitSeller);

		SubmitSource submitSource = getSubmitSource(shoppingCart);
		submitOrder.setSource(submitSource);

		SubmitUmp submitUmp = getSubmitUmp(shoppingCart);
		submitOrder.setUmp(submitUmp);

		String submitOrder_s = JSONObject.toJSONString(submitOrder);
		logger.info(submitOrder_s);

		grabDao.updateStatusById(shoppingCart.getId(), 2, null, shoppingCart.getRemark());

		logger.info("准备抢单:" + shoppingCart.getTitle());
		Map<String, Object> taskMap = new HashMap<String, Object>();
		taskMap.put("ojb", submitOrder_s);

		taskMap.put("time", grabTime);
		taskMap.put("id", shoppingCart.getId());
		taskMap.put("remark", shoppingCart.getRemark());

		grabTask.executor(2, taskMap);

	}

	/**
	 * Ump
	 * 
	 * @param shoppingCart
	 * @return
	 */
	private SubmitUmp getSubmitUmp(ShoppingCart shoppingCart) {
		SubmitUmp submitUmp = new SubmitUmp();

		SubmitActivitie submitActivitie = new SubmitActivitie();
		submitActivitie.setGoodsId(shoppingCart.getGoods_id());
		submitActivitie.setKdtId(shoppingCart.getKdt_id());
		submitActivitie.setSkuId(shoppingCart.getSku_id());

		List<SubmitActivitie> submitActivitieList = new ArrayList<SubmitActivitie>();
		submitActivitieList.add(submitActivitie);

		submitUmp.setActivities(submitActivitieList);

		SubmitUseCustomerCardInfo submitUseCustomerCardInfo = new SubmitUseCustomerCardInfo();
		submitUmp.setUseCustomerCardInfo(submitUseCustomerCardInfo);

		return submitUmp;
	}

	/**
	 * SubmitSource
	 * 
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	private SubmitSource getSubmitSource(ShoppingCart shoppingCart) throws Exception {
		SubmitSource submitSource = new SubmitSource();

		SubmitItemSource submitItemSource = new SubmitItemSource();
		submitItemSource.setCartCreateTime(shoppingCart.getCreated_time());
		submitItemSource.setCartUpdateTime(shoppingCart.getUpdated_time());
		submitItemSource.setGoodsId(shoppingCart.getGoods_id());
		submitItemSource.setSkuId(shoppingCart.getSku_id());

		List<SubmitItemSource> SubmitItemSourceList = new ArrayList<SubmitItemSource>();
		SubmitItemSourceList.add(submitItemSource);

		submitSource.setItemSources(SubmitItemSourceList);

		submitSource.setKdtSessionId(shoppingCart.getNobody());
		return submitSource;
	}

	/**
	 * 构建SubmitSeller
	 * 
	 * @param shoppingCart
	 * @return
	 */
	private SubmitSeller getSubmitSeller(ShoppingCart shoppingCart) {
		SubmitSeller submitSeller = new SubmitSeller();
		submitSeller.setKdtId(shoppingCart.getKdt_id());
		return submitSeller;
	}

	/**
	 * 构建SubmitItems
	 * 
	 * @param shoppingCart
	 * @return
	 */
	private List<SubmitItem> getSubmitItems(ShoppingCart shoppingCart) {
		List<SubmitItem> submitItems = new ArrayList<SubmitItem>();

		SubmitItem submitItem = new SubmitItem();
		submitItem.setGoodsId(shoppingCart.getGoods_id());
		submitItem.setKdtId(shoppingCart.getKdt_id());
		submitItem.setNum(shoppingCart.getNum());
		submitItem.setSkuId(shoppingCart.getSku_id());

		submitItems.add(submitItem);

		return submitItems;
	}

	/**
	 * 构建SubmitDelivery
	 * 
	 * @param shoppingCart
	 * @param user
	 * @return
	 */
	private SubmitDelivery getSubmitDelivery(ShoppingCart shoppingCart, User user) {
		SubmitDelivery submitDelivery = new SubmitDelivery();

		SubmitAddress submitAddress = new SubmitAddress();
		submitAddress.setAddressDetail(user.getAddressDetail());
		submitAddress.setCity(user.getCity());
		submitAddress.setCounty(user.getCounty());
		submitAddress.setId(user.getBuyerId().toString());
		submitAddress.setProvince(user.getProvince());
		submitAddress.setRecipients(user.getRealName());
		submitAddress.setTel(user.getTel());
		submitAddress.setUserId(shoppingCart.getBuyer_id().toString());
		submitAddress.setUserName(user.getRealName());
		submitDelivery.setAddress(submitAddress);

		return submitDelivery;
	}

	@Override
	public void doGrab(Object obj) throws InterruptedException {
		String cookies = userDao.getAttribute("cookies");
		String url = "https://cashier.youzan.com/pay/wsctrade/order/buy/v2/bill.json";
		Map<String, Object> objMap = (Map<String, Object>) obj;
		String param = (String) objMap.get("ojb");
		Long time = (Long) objMap.get("time");
		Integer id = (Integer) objMap.get("id");
		String remark = (String) objMap.get("remark");

		Long curTime = null;
		// 判断时间
		while (true) {
			curTime = System.currentTimeMillis();
			if (curTime >= time) {
				break;
			} else {
				Thread.sleep(500);
			}
		}

		// 最多抢3次
		String postJson = null;
		JSONObject parseObject = null;
		Integer code = null;
		JSONObject data = null;
		String orderNo = null;
		String msg = null;
		for (int i = 0; i < 180; i++) {

			postJson = HttpUntil.postJson(url, param, cookies, null);
			logger.info("正在抢单,url：" + url + ",param:" + param + ",res:" + postJson);

			parseObject = JSONObject.parseObject(postJson);
			// 出现异常
			if (parseObject == null) {
				Thread.sleep(1000);
				continue;
			}

			code = parseObject.getInteger("code");
			msg = parseObject.getString("msg");
			// 出现异常
			if (code == null) {
				Thread.sleep(1000);
				continue;
			}

			// 已经抢完
			if (code == 101910001) {
				break;
			}

			if (code != 0) {
				Thread.sleep(1000);
				continue;
			}

			// if (!"ok".equals(msg)) {
			// Thread.sleep(1000);
			// continue;
			// }

			data = parseObject.getJSONObject("data");
			orderNo = data.getString("orderNo");
			if (orderNo != null) {
				grabDao.updateStatusById(id, 3, orderNo, remark);
				return;
			}
		}

		grabDao.updateStatusById(id, 4, null, remark + ";" + msg);
	}

	@Override
	public void deleteGrab(Integer id) {
		grabDao.deleteGrab(id);

	}

	@Override
	public void doGrabNew(Object obj) throws InterruptedException {
		String authorization = userDao.getAttribute("authorization");
		String url = baiNewUrl + "/backend/order";

		Map<String, Object> objMap = (Map<String, Object>) obj;
		String param = (String) objMap.get("ojb");
		Long time = (Long) objMap.get("time");
		Integer id = (Integer) objMap.get("id");
		String remark = (String) objMap.get("remark");

		Long curTime = null;
		// 判断时间
		while (true) {
			curTime = System.currentTimeMillis();
			if (curTime >= time) {
				break;
			} else {
				Thread.sleep(500);
			}
		}

		// 最多抢3次
		String postJson = null;
		JSONObject parseObject = null;
		Integer code = null;
		JSONObject data = null;
		String orderNo = null;
		String msg = null;
		for (int i = 0; i < 180; i++) {
			postJson = HttpUntil.postJson(url, param, null, authorization);
			logger.info("正在抢单,url:" + url + ",param:" + param + ",res:" + postJson);

			parseObject = JSONObject.parseObject(postJson);
			// 出现异常
			if (parseObject == null) {
				Thread.sleep(1000);
				continue;
			}

			code = parseObject.getInteger("code");
			msg = parseObject.getString("message");
			// 出现异常
			if (code == null) {
				Thread.sleep(1000);
				continue;
			}

			// 已经抢完
			if (code == 80001) {
				Thread.sleep(1000);
				continue;
			}

			if (code != 0) {
				Thread.sleep(1000);
				continue;
			}

			data = parseObject.getJSONObject("data");
			orderNo = data.getString("orderId");
			if (orderNo != null) {
				grabDao.updateStatusById(id, 3, orderNo, remark);
				return;
			}
		}

		grabDao.updateStatusById(id, 4, null, remark + ";" + msg);

	}

	@Override
	public MessageEnum addGrabNew(Integer time1, Integer time2, Integer num, Integer userId, String goodName,
	        String sku, String remark, Integer fromType, Integer preId, Integer belongId) {

		ShoppingCart shoppingCart = new ShoppingCart();

		shoppingCart.setTitle(goodName.trim());
		if (!CommonUntil.isEmptyStr(sku)) {
			shoppingCart.setSku(sku.trim());
		}

		User user = new User();
		user.setId(userId);

		shoppingCart.setUser(user);
		shoppingCart.setNum(num);

		SimpleDateFormat formatShort = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateS = formatShort.format(date);

		String hour = time1 + "";
		if (time1 < 10) {
			hour = "0" + time1;
		}
		String minte = time2 + "";
		if (time2 < 10) {
			minte = "0" + time2;
		}
		String second = "00";

		dateS = dateS + " " + hour + ":" + minte + ":" + second;
		shoppingCart.setGrabTime(dateS);

		shoppingCart.setFromType(fromType);
		shoppingCart.setBelongId(belongId);

		grabDao.saveShoppingCart(shoppingCart, remark);
		if (preId != null) {
			grabDao.deleteGrab(preId);
		}

		return MessageEnum.SUCCESS;
	}

	@Override
	public void nowGrab(Integer id) {
		SimpleDateFormat formatShort = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateS = formatShort.format(date);
		grabDao.updateGrabTimeById(id, dateS);

	}

	@Override
	public void reGrab(Integer id) {
		grabDao.updateStatusById(id, 1, null, null);

	}

	@Override
	public ShoppingCart setDefaultGood(Integer fromType, Integer id) {
		if (id == null) {
			return grabDao.getLastGrabList(fromType);
		} else {
			ShoppingCart grab = grabDao.getGrabById(id);
			String grabTime = grab.getGrabTime();
			String[] split1 = grabTime.split(" ");
			String[] split2 = split1[1].split(":");
			grab.setGrabTimeH(split2[0]);
			grab.setGrabTimeM(split2[1]);
			return grab;
		}

	}

	public static void main(String[] args) {
		String authorization = "bearereyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxODYwNjk3Mjc2MiIsInNjb3BlIjpbInNlcnZpY2UiXSwiaWQiOiIwMzMxZjZjOS05NDk4LTc1NjktMDUwMi1lNTI4ODA3NjlhZTQiLCJleHAiOjE1OTI0NzY5NzAsInR5cGUiOiJtZW1iZXIiLCJhdXRob3JpdGllcyI6WyIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiXSwianRpIjoiOThjMjY5YTYtMTlmZi00MGNhLWJjMzEtYTFmZGNjYzNjYWRlIiwiY2xpZW50X2lkIjoiaW5uZXIifQ.M7wgorAvOakfomCJjreLLH8WCrAkIvCCZOKZhq68aFz7O2vAc6cKLEbmSNQgKTB84__7len4aZUUomOxJz_BT5oVuun0ktg5BYbSViIXUetAdfZr5RNijS9wwwxgz51Zx0nM9oTOBy08cNjU3J2f7vd2o-C-hqxK3PLZWeYZUHTjfgiR4GpbV27MdWwaSE5agM_l-zBJWrYAMYmSnR1qbogeOrEzU_64pFYZwpmiDrKCXBj2PM2rtxpLOnnqakLXdUlnsTWMzSPItG4p1ONADSRKpGoIP103xs7J66crQDOjssOnvX3bhvEEIvfZDMnUuxUV5OQCd7iURhH3hVl1pg";
		String url = "https://backend.bqjapp.cn" + "/backend/order";
		String param = "{\"area\":\"冷水滩区\",\"city\":\"永州市\",\"discountGoodsList\":[{\"goodsList\":[{\"count\":\"1\",\"goodId\":\"6c61b2b3-4924-47aa-be6b-6cf7997177a1\",\"name\":\"DHBJ 6099#速干万能裤，6月4日-6月7日左右发货\",\"spec1\":\"2XL\",\"spec2\":\"\",\"spec3\":\"\"}]}],\"goodsList\":[{\"count\":\"1\",\"goodId\":\"6c61b2b3-4924-47aa-be6b-6cf7997177a1\",\"image\":\"http://img.bqjapp.cn/M00/3A/BB/wKgLKV7Drp-AQQKNAAAibd_7SS8326.jpg\",\"name\":\"DHBJ 6099#速干万能裤，6月4日-6月7日左右发货\",\"spec1\":\"2XL\",\"spec2\":\"\",\"spec3\":\"\"}],\"houseNumber\":\"湖南省永州市冷水滩区六月岭巷九号三医院后门老电影院宿舍\",\"postFee\":\"0\",\"price\":\"49.00\",\"province\":\"湖南省\",\"receiverName\":\"蔡玉萍\",\"receiverPhone\":\"13574612135\",\"remark\":\"\",\"street\":\"\"}";

		String postJson = HttpUntil.postJson(url, param, null, authorization);
		System.out.println(postJson);
	}
}
