package cyyGroup.cyyArt.money.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.dao.UserDao;
import cyyGroup.cyyArt.money.service.UserNewService;
import cyyGroup.cyyArt.until.CommonUntil;
import cyyGroup.cyyArt.until.CyyArtException;
import cyyGroup.cyyArt.until.HttpUntil;
import cyyGroup.cyyArt.vo.User;

@Service
public class UserNewServiceImpl implements UserNewService {

	@Autowired
	private UserDao userDao;

	@Value("${bai.new.url}")
	private String baiNewUrl;

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public void testSyn() {
		List<User> userList = userDao.getUserList(9);
		String authorization = userDao.getAttribute("authorization");
		String userId = userDao.getAttribute("userid");
		String url = baiNewUrl + "/backend/member/" + userId + "/address";
		Map<String, String> map = null;
		for (User user : userList) {
			map = new HashMap<String, String>();
			map.put("receiveName", user.getRealName());
			map.put("receivePhone", user.getTel());
			map.put("province", user.getProvince());
			map.put("city", user.getCity());
			map.put("area", user.getCounty());
			map.put("address", user.getAddressDetail());

			HttpUntil.postMap(url, map, authorization);
		}

	}

	@Override
	public void synBuyer(Integer belongId) throws Exception {
		String authorization = userDao.getAttribute("authorization");
		String userId = userDao.getAttribute("userid");
		String url = baiNewUrl + "/backend/member/" + userId + "/address";

		// 先取出自己已经存在的
		Set<String> hasSet = getUserHasSet(belongId);

		String res = null;
		JSONObject jsonObj = null;
		Integer code = null;
		JSONObject data = null;
		JSONArray rows = null;
		JSONObject adreInfo = null;
		User user = null;
		Integer total = 0;

		String realName = null;
		String tel = null;
		String addressDetail = null;
		String checkSign = null;

		// 默认取100次
		String urlCur = null;
		for (int i = 1; i < 100; i++) {
			urlCur = url + "?pageSize=20&pageIndex=" + i;
			res = HttpUntil.get(urlCur, null, authorization);
			logger.info("synBuyer url is:" + url + ";res is:" + res);

			if (res == null) {
				throw new Exception("接口异常");
			}

			jsonObj = JSONObject.parseObject(res);
			code = jsonObj.getInteger("code");
			if (code != 0) {
				throw new Exception("接口异常");
			}

			data = jsonObj.getJSONObject("data");
			rows = data.getJSONArray("rows");

			for (Object object : rows) {
				adreInfo = (JSONObject) object;

				realName = adreInfo.getString("receiveName");
				tel = adreInfo.getString("receivePhone").replaceAll(" ", "");
				addressDetail = adreInfo.getString("address");

				checkSign = realName + tel + addressDetail;
				if (hasSet.contains(checkSign)) {
					continue;
				}

				user = new User();
				user.setBelongId(belongId);
				user.setType(1);
				user.setUserName(realName);
				user.setTel(tel);
				user.setProvince(adreInfo.getString("province"));
				user.setCity(adreInfo.getString("city"));
				user.setCounty(adreInfo.getString("area"));
				user.setAddressDetail(addressDetail);

				userDao.saveUser(user);
			}

			// 看看还有没有下一条
			total = data.getInteger("total");
			if (total <= 20 * i) {
				break;
			}
		}

	}

	/**
	 * 取出自己已经存在的
	 * 
	 * @param belongId
	 * @return
	 */
	private Set<String> getUserHasSet(Integer belongId) {
		Set<String> hasSet = new HashSet<String>();

		List<User> userList = userDao.getUserList(belongId);
		if (CommonUntil.isEmptyList(userList)) {
			return hasSet;
		}

		String realName = null;
		String tel = null;
		String addressDetail = null;
		String checkSign = null;
		for (User user : userList) {
			realName = user.getRealName();
			tel = user.getTel();
			addressDetail = user.getAddressDetail();

			checkSign = realName + tel + addressDetail;
			hasSet.add(checkSign);
		}
		return hasSet;
	}

	@Override
	public MessageEnum loginBai(Integer belongId, String name, String password) throws CyyArtException, Exception {
		String url = baiNewUrl + "/backend/shm/longin";
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", name);
		map.put("password", password);
		map.put("registrationId", "");
		map.put("type", "member");

		String postJson = HttpUntil.postMap(url, map, null);
		logger.info("正在登录白,url:" + url + ",param:" + JSONObject.toJSONString(map) + ",res:" + postJson);
		JSONObject parseObject = JSONObject.parseObject(postJson);

		if (parseObject == null) {
			throw new CyyArtException("登录失败请联系管理员");
		}

		Integer code = parseObject.getInteger("code");
		// String msg = parseObject.getString("message");

		if (20008 == code) {
			return MessageEnum.ERRORPASS;
		} else if (20004 == code) {
			return MessageEnum.USER_NOTEXIST;
		} else if (0 == code) {
			JSONObject data = parseObject.getJSONObject("data");
			JSONObject jwt = data.getJSONObject("jwt");
			String token_type = jwt.getString("token_type");
			String access_token = jwt.getString("access_token");

			userDao.updateAttr(belongId, "authorization", token_type + access_token);

			return MessageEnum.SUCCESS;
		}

		return null;
	}

}
