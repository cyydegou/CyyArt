package cyyGroup.cyyArt.money.service;

import java.util.List;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.vo.User;

public interface UserService {

	/**
	 * 通过用户名密码得到用户列表
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	List<User> getUserByNamePass(String name, String password);

	/**
	 * 同步
	 */
	void synBuyer();

	/**
	 * 用户列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<User> getUserList(Integer userId, Integer pageNo, Integer pageSize);

	/**
	 * 清除选择
	 */
	void clearChoice();

	/**
	 * 挑选用户
	 * 
	 * @param checkList
	 */
	void choiceUsers(List<User> checkList);

	/**
	 * 设置默认
	 * 
	 * @param id
	 * @param nowDefault
	 * @return
	 */
	void changeDefault(Integer id, Integer nowDefault);

	/**
	 * 得到默认用户
	 * 
	 * @param preUserId
	 * 
	 * @return
	 */
	User setDefaultuser(Integer preUserId);

	/**
	 * 测试新平台登录
	 * 
	 * @return
	 */
	MessageEnum testConnectNew();
}
