package cyyGroup.cyyArt.money.service;

import java.util.List;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.vo.ShoppingCart;

/**
 * 抢单
 * 
 * @author caiyy
 *
 * @date 2019年8月7日
 */
public interface GrabService {

	/**
	 * 抢单列表
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	List<ShoppingCart> getGrabList(Integer fromType, Integer userId);

	/**
	 * 购物车
	 * 
	 * @param fromType
	 * 
	 * @return
	 */
	List<ShoppingCart> getCartlist();

	/**
	 * 增加抢单
	 * 
	 * @param time1
	 * @param time2
	 * @param time3
	 * @param userId2
	 * @param alias
	 * @param remark
	 * @return
	 */
	MessageEnum addGrab(Integer time1, Integer time2, Integer time3, Integer num, String alias,
	        String remark);

	/**
	 * 准备抢单
	 * 
	 * @throws Exception
	 */
	void prepareGrab() throws Exception;

	/**
	 * 执行抢单
	 * 
	 * @param obj
	 * @throws InterruptedException
	 */
	void doGrab(Object obj) throws InterruptedException;

	/**
	 * 删除抢单
	 * 
	 * @param id
	 */
	void deleteGrab(Integer id);

	/**
	 * 新的抢单
	 * 
	 * @param obj
	 */
	void doGrabNew(Object obj) throws InterruptedException;

	/**
	 * 加入抢单 新平台
	 * 
	 * @param time1
	 * @param time2
	 * @param num
	 * @param userId
	 * @param goodName
	 * @param sku
	 * @param remark
	 * @param fromType
	 * @param preId
	 * @param belongId
	 * @return
	 */
	MessageEnum addGrabNew(Integer time1, Integer time2, Integer num, Integer userId, String goodName, String sku,
	        String remark, Integer fromType, Integer preId, Integer belongId);

	void nowGrab(Integer id);

	/**
	 * 重新启动抢单
	 * 
	 * @param id
	 */
	void reGrab(Integer id);

	/**
	 * 得到最近的新平台抢单
	 * 
	 * @param fromType
	 * @param id
	 * 
	 * @return
	 */
	ShoppingCart setDefaultGood(Integer fromType, Integer id);
}
