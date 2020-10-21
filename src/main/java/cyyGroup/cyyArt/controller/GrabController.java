package cyyGroup.cyyArt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.service.GrabService;
import cyyGroup.cyyArt.until.CacheUntil;
import cyyGroup.cyyArt.vo.Response;
import cyyGroup.cyyArt.vo.ShoppingCart;
import cyyGroup.cyyArt.vo.User;

@RestController
@RequestMapping("/grab")
public class GrabController {

	@Autowired
	private GrabService grabService;

	private Log logger = LogFactory.getLog(getClass());

	@RequestMapping("/grablist.json")
	public List<ShoppingCart> grabList(HttpServletRequest request,
	        @RequestParam(required = true, name = "fromType") Integer fromType) {
		try {

			User user = (User) request.getSession().getAttribute("user");
			List<ShoppingCart> orderList = grabService.getGrabList(fromType, user.getId());
			return orderList;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

	}

	@RequestMapping("/cartlist.json")
	public List<ShoppingCart> cartList() {
		try {
			List<ShoppingCart> cartList = grabService.getCartlist();
			CacheUntil.tempCartList = cartList;
			return cartList;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

	}

	@RequestMapping("/addgrab.json")
	public Response<String> addGrab(@RequestParam(required = false, name = "time1") Integer time1,
	        @RequestParam(required = true, name = "time2") Integer time2,
	        @RequestParam(required = true, name = "num") Integer num,
	        @RequestParam(required = true, name = "userId") Integer userId,
	        @RequestParam(required = true, name = "alias") String alias,
	        @RequestParam(required = true, name = "remark") String remark) {
		Response<String> res = null;
		try {
			MessageEnum resEnum = grabService.addGrab(time1, time2, num, userId, alias, remark);
			res = new Response<String>(resEnum);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 抢单，新平台
	 * 
	 * @param time1
	 * @param time2
	 * @param num
	 * @param userId
	 * @param alias
	 * @param remark
	 * @return
	 */
	@RequestMapping("/addgrabnew.json")
	public Response<String> addGrabNew(@RequestParam(required = false, name = "time1") Integer time1,
	        @RequestParam(required = true, name = "time2") Integer time2,
	        @RequestParam(required = true, name = "num") Integer num,
	        @RequestParam(required = true, name = "userId") Integer userId,
	        @RequestParam(required = true, name = "goodName") String goodName,
	        @RequestParam(required = true, name = "sku") String sku,
	        @RequestParam(required = true, name = "remark") String remark,
	        @RequestParam(required = true, name = "fromType") Integer fromType,
	        @RequestParam(required = false, name = "preId") Integer preId,
	        HttpServletRequest request) {
		Response<String> res = null;
		try {

			User user = (User) request.getSession().getAttribute("user");
			MessageEnum resEnum = grabService.addGrabNew(time1, time2, num, userId, goodName, sku, remark, fromType,
			        preId, user.getId());
			res = new Response<String>(resEnum);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deletegrab.json")
	public Response<String> deleteGrab(@RequestParam(required = false, name = "id") Integer id) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			grabService.deleteGrab(id);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 设置默认商品-有赞-半自动
	 * 
	 * @return
	 */
	@RequestMapping("/setdefaultgoods.json")
	public Response<List<ShoppingCart>> setDefaultGoods() {
		Response<List<ShoppingCart>> res = new Response<List<ShoppingCart>>(MessageEnum.SUCCESS);
		try {
			List<ShoppingCart> cartList = grabService.getCartlist();
			CacheUntil.tempCartList = cartList;
			res.setData(cartList);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<List<ShoppingCart>>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 现在抢单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/nowgrab.json")
	public Response<String> nowGrab(@RequestParam(required = false, name = "id") Integer id) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			grabService.nowGrab(id);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 重新启动抢单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/regrab.json")
	public Response<String> reGrab(@RequestParam(required = false, name = "id") Integer id) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			grabService.reGrab(id);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 设置默认商品：新app
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/setdefaultgood.json")
	public Response<ShoppingCart> setDefaultGood(@RequestParam(required = false, name = "fromType") Integer fromType,
	        @RequestParam(required = false, name = "id") Integer id) {
		Response<ShoppingCart> res = new Response<ShoppingCart>(MessageEnum.SUCCESS);
		try {
			ShoppingCart shoppingCart = grabService.setDefaultGood(fromType, id);
			res.setData(shoppingCart);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<ShoppingCart>(MessageEnum.FAIL);
			return res;
		}

	}
}
