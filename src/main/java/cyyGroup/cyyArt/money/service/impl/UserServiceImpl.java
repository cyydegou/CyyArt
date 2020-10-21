package cyyGroup.cyyArt.money.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.dao.UserDao;
import cyyGroup.cyyArt.money.service.UserService;
import cyyGroup.cyyArt.until.HttpUntil;
import cyyGroup.cyyArt.vo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Value("${application.password.key}")
	private String passKey;

	@Value("${bai.new.url}")
	private String baiNewUrl;

	@Override
	public List<User> getUserByNamePass(String name, String password) {
		password = password + passKey;
		String encode = DigestUtils.md5DigestAsHex(password.getBytes());
		List<User> userList = userDao.getUserByNamePass(name, encode);
		return userList;
	}

	@Override
	public void synBuyer() {
		// 接口请求
		String cookies = userDao.getAttribute("cookies");
		String html = HttpUntil.get("https://h5.youzan.com/wsctrade/order/address/list?switchable=false",
		        cookies, null);
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
		System.out.println(split2[1]);
		JSONObject objAll = JSONObject.parseObject(split2[1].trim());
		JSONArray objAdressList = objAll.getJSONArray("addressList");
		List<User> userList = JSONObject.parseArray(objAdressList.toString(), User.class);
		System.out.println(objAdressList);
		List<User> userListTemp = null;

		String tel = null;
		for (User user : userList) {
			userListTemp = userDao.getUserByBuyerId(user.getId());
			if (userListTemp.size() > 0) {
				continue;
			}
			user.setType(1);
			// 把电话号码空格去掉
			tel = user.getTel();
			tel = tel.replace(" ", "");
			user.setTel(tel);

			userDao.saveUser(user);
		}

	}

	@Override
	public List<User> getUserList(Integer userId, Integer pageNo, Integer pageSize) {
		return userDao.getUserList(userId);
	}

	@Override
	public void clearChoice() {
		userDao.clearChoice();

	}

	@Override
	public void choiceUsers(List<User> checkList) {
		for (User user : checkList) {
			userDao.updateChoice(user);
		}

	}

	@Override
	public void changeDefault(Integer id, Integer nowDefault) {
		// 只能设置一个默认
		if (nowDefault == 0) {
			userDao.updateDefault(null, 0);
			userDao.updateDefault(id, 1);
		} else {
			userDao.updateDefault(id, 0);
		}

	}

	@Override
	public User setDefaultuser(Integer preUserId) {
		if (preUserId == null) {
			return userDao.getUserByDefault();
		} else {
			return userDao.getUserById(preUserId);
		}

	}

	@Override
	public MessageEnum testConnectNew() {
		String authorization = userDao.getAttribute("authorization");
		String userid = userDao.getAttribute("userid");

		String url = baiNewUrl + "/backend/shoppingCart/" + userid;
		String res = HttpUntil.get(url, null, authorization);
		if (res == null) {
			return MessageEnum.NEW_NOTLOGIN;
		}

		JSONObject objAll = JSONObject.parseObject(res);
		Integer code = objAll.getInteger("code");
		if (code == null || code != 0) {
			return MessageEnum.NEW_NOTLOGIN;
		}

		return MessageEnum.SUCCESS;
	}

}
