package cyyGroup.cyyArt.money.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.vo.Express;
import cyyGroup.cyyArt.vo.Order;
import cyyGroup.cyyArt.vo.Response;

public interface OrderService {

	/**
	 * 得到订单列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Order> getOrderList(String param);

	/**
	 * 保存订单
	 * 
	 * @param mulRequest
	 * @param order
	 * @return
	 * @throws Exception
	 */
	void saveOrder(MultipartHttpServletRequest mulRequest, Order order) throws Exception;

	/**
	 * 同步订单
	 * 
	 * @param i
	 * 
	 * @throws UnsupportedEncodingException
	 */
	MessageEnum synOrders(int belongId, Integer pageNo) throws UnsupportedEncodingException;

	/**
	 * 查出未付款订单数
	 * 
	 * @return
	 */
	HashMap<String, Object> checkNotPay();

	/**
	 * 更新顾客还款情况
	 * 
	 * @param id
	 * @param prePay
	 * @param money
	 * @return
	 */
	MessageEnum changePay(Integer id, Integer prePay, String money);

	/**
	 * 得到物流信息
	 * 
	 * @param id
	 * @return
	 */
	Response<Express> getExp(Integer id);

	void keepAlive();

	/**
	 * 检查白那里有没有登录
	 * 
	 * @return
	 */
	MessageEnum checkBaiLogin();

}
