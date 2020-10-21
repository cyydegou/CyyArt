package cyyGroup.cyyArt.money.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cyyGroup.cyyArt.vo.Order;

@Mapper
public interface OrderDao {

	/**
	 * 得到订单列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Order> getOrderList(@Param(value = "pay") Integer pay);

	/**
	 * 保存订单
	 * 
	 * @param order
	 */
	void saveOrder(@Param(value = "order") Order order);

	/**
	 * 根据白的id得到订单列表
	 * 
	 * @param baiId
	 * @return
	 */
	List<Order> getOrderListByBaiId(@Param(value = "baiId") String baiId);

	/**
	 * 查出未付款订单数
	 * 
	 * @return
	 */
	HashMap<String, Object> getNotPayCount();

	/**
	 * 更新顾客还款情况
	 * 
	 * @param id
	 * @param i
	 * @param money
	 */
	void updateOrderPay(@Param(value = "id") Integer id, @Param(value = "pay") int pay,
	        @Param(value = "money") String money);

	/**
	 * 根据白id更新状态
	 * 
	 * @param baiId
	 * @param expressUrl
	 * @param integer
	 */
	void updateOrderStatusByBaiId(@Param(value = "baiId") String baiId, @Param(value = "status") Integer status,
	        @Param(value = "expressUrl") String expressUrl);

	/**
	 * 得到订单通过id
	 * 
	 * @param id
	 * @return
	 */
	Order getOrderListById(@Param(value = "id") Integer id);

}
