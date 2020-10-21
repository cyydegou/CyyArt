package cyyGroup.cyyArt.money.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cyyGroup.cyyArt.vo.User;

@Mapper
public interface UserDao {

	/**
	 * 通过用户名密码得到用户列表
	 * 
	 * @param name
	 * @param encode
	 * @return
	 */
	List<User> getUserByNamePass(@Param(value = "name") String name, @Param(value = "encode") String encode);

	/**
	 * 通过购买者的id得到用户列表
	 * 
	 * @param id
	 * @return
	 */
	List<User> getUserByBuyerId(@Param(value = "id") Integer id);

	/**
	 * 保存用户
	 * 
	 * @param user
	 */
	void saveUser(@Param(value = "user") User user);

	/**
	 * 顾客列表
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	List<User> getUserList(@Param(value = "userId") Integer userId);

	/**
	 * 根据属性得到值
	 * 
	 * @param string
	 * @return
	 */
	String getAttribute(@Param(value = "key") String key);

	/**
	 * 根据用户真名和电话查用户
	 * 
	 * @param name
	 * @param tel
	 * @return
	 */
	List<User> getUserListByRealNameTel(@Param(value = "name") String name, @Param(value = "tel") String tel);

	/**
	 * 更新ktd——id的属性，防止变化
	 * 
	 * @param string
	 * @param kdt_id
	 */
	void updateAttr(@Param(value = "belongId") Integer belongId, @Param(value = "attr") String attr,
	        @Param(value = "kdt_id") String kdt_id);

	/**
	 * 拿到User
	 * 
	 * @param userId
	 * @return
	 */
	User getUserById(@Param(value = "userId") Integer userId);

	/**
	 * 清除选择
	 */
	void clearChoice();

	/**
	 * 更新挑选用户
	 * 
	 * @param user
	 */
	void updateChoice(@Param(value = "user") User user);

	/**
	 * 只能设置一个默认
	 * 
	 * @param id
	 * @param i
	 */
	void updateDefault(@Param(value = "id") Integer id, @Param(value = "type") int type);

	/**
	 * 得到默认用户
	 * 
	 * @return
	 */
	User getUserByDefault();

}
