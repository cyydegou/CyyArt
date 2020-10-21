package cyyGroup.cyyArt.money.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cyyGroup.cyyArt.vo.ShoppingCart;

@Mapper
public interface GrabDao {

	/**
	 * 抢单列表
	 * 
	 * @param fromTypes
	 * @param userId
	 * 
	 * @return
	 */
	List<ShoppingCart> getGrabList(@Param(value = "fromTypes") List<Integer> fromTypes,
	        @Param(value = "userId") Integer userId);

	/**
	 * 保存订单
	 * 
	 * @param shoppingCart
	 * @param remark
	 */
	void saveShoppingCart(@Param(value = "shoppingCart") ShoppingCart shoppingCart,
	        @Param(value = "remark") String remark);

	/**
	 * 通过状态得到购物车
	 * 
	 * @param status
	 * @return
	 */
	List<ShoppingCart> getGrabListByStatus(@Param(value = "status") int status);

	/**
	 * 删除抢单
	 * 
	 * @param id
	 */
	void deleteGrab(@Param(value = "id") Integer id);

	/**
	 * 改抢单状态
	 * 
	 * @param id
	 * @param orderNo
	 * @param remark
	 * @param i
	 */
	void updateStatusById(@Param(value = "id") Integer id, @Param(value = "status") int status,
	        @Param(value = "orderNo") String orderNo, @Param(value = "remark") String remark);

	/**
	 * 现在抢单--改时间
	 * 
	 * @param id
	 * @param dateS
	 */
	void updateGrabTimeById(@Param(value = "id") Integer id, @Param(value = "dateS") String dateS);

	/**
	 * 得到最近的新平台抢单
	 * 
	 * @param i
	 * @return
	 */
	ShoppingCart getLastGrabList(@Param(value = "fromType") int fromType);

	/**
	 * 用id得到最近的新平台抢单
	 * 
	 * @param id
	 * @return
	 */
	ShoppingCart getGrabById(@Param(value = "id") Integer id);
}
