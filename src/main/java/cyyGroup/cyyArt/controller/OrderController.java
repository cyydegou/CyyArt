package cyyGroup.cyyArt.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.service.OrderService;
import cyyGroup.cyyArt.until.CacheUntil;
import cyyGroup.cyyArt.vo.Express;
import cyyGroup.cyyArt.vo.Order;
import cyyGroup.cyyArt.vo.Response;
import cyyGroup.cyyArt.vo.User;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping("/orderlist.json")
	public List<Order> orderList(@RequestParam(required = false, name = "param") String param,
	        HttpServletRequest request) {
		try {
			List<Order> orderList = orderService.getOrderList(param);

			return orderList;
		} catch (Exception e) {
			return null;
		}

	}

	@RequestMapping(value = "/addorder.json", produces = "application/json;charset=UTF-8")
	public Response<String> addOrder(@RequestParam(required = true, name = "name") String name,
	        @RequestParam(required = true, name = "userId") Integer userId,
	        @RequestParam(required = true, name = "color") String color,
	        @RequestParam(required = true, name = "size") Integer size,
	        @RequestParam(required = true, name = "remark") String remark,
	        HttpServletRequest request, HttpServletResponse response) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {

			Order order = new Order();
			order.setName(name);
			order.setColor(color);
			order.setSize(size);
			order.setRemark(remark);

			User user = new User();
			user.setId(userId);
			order.setUser(user);

			// 从请求中获取到文件信息需要将请求转换为MultipartHttpServletRequest类型
			MultipartHttpServletRequest MulRequest = (MultipartHttpServletRequest) request;

			orderService.saveOrder(MulRequest, order);
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
		}

		return res;
	}

	/**
	 * 同步订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/synorders.json", produces = "application/json;charset=UTF-8")
	public Response<String> synOrders(HttpServletRequest request) {
		Response<String> res = null;
		try {
			User user = (User) request.getSession().getAttribute("user");

			CacheUntil.hasSynOrderNum = 0;
			MessageEnum synOrders = orderService.synOrders(user.getId(), 1);
			res = new Response<String>(synOrders);
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
		}
		return res;
	}

	/**
	 * 检查有没有没有付款的订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checknotpay.json", produces = "application/json;charset=UTF-8")
	public Response<HashMap<String, Object>> checkNotPay() {
		Response<HashMap<String, Object>> res = new Response<HashMap<String, Object>>(MessageEnum.SUCCESS);
		try {
			HashMap<String, Object> count = orderService.checkNotPay();
			res.setData(count);
		} catch (Exception e) {
			logger.error(e);
			res = new Response<HashMap<String, Object>>(MessageEnum.FAIL);
		}

		return res;
	}

	/**
	 * 更新顾客还款情况
	 * 
	 * @return
	 */
	@RequestMapping(value = "/changepay.json", produces = "application/json;charset=UTF-8")
	public Response<Integer> changePay(@RequestParam(required = true, name = "id") Integer id,
	        @RequestParam(required = true, name = "prePay") Integer prePay,
	        @RequestParam(required = false, name = "money") String money) {
		Response<Integer> res = null;
		try {
			MessageEnum resEnum = orderService.changePay(id, prePay, money);
			res = new Response<Integer>(resEnum);
		} catch (Exception e) {
			logger.error(e);
			res = new Response<Integer>(MessageEnum.FAIL);
		}

		return res;
	}

	/**
	 * 得到物流信息
	 * 
	 * @param id
	 * @param prePay
	 * @param money
	 * @return
	 */
	@RequestMapping(value = "/getexp.json", produces = "application/json;charset=UTF-8")
	public Response<Express> getExp(@RequestParam(required = true, name = "id") Integer id) {
		Response<Express> res = null;
		try {
			res = orderService.getExp(id);
		} catch (Exception e) {
			logger.error(e);
			res = new Response<Express>(MessageEnum.FAIL);
		}

		return res;
	}

	/**
	 * 检查白那里有没有登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkbailogin.json", produces = "application/json;charset=UTF-8")
	public Response<Integer> checkBaiLogin() {
		Response<Integer> res = null;
		try {
			MessageEnum resEnum = orderService.checkBaiLogin();
			res = new Response<Integer>(resEnum);
		} catch (Exception e) {
			logger.error(e);
			res = new Response<Integer>(MessageEnum.FAIL);
		}

		return res;
	}
}
