package cyyGroup.cyyArt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.service.UserNewService;
import cyyGroup.cyyArt.money.service.UserService;
import cyyGroup.cyyArt.vo.Response;
import cyyGroup.cyyArt.vo.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserNewService userNewService;

	private Log logger = LogFactory.getLog(getClass());

	/**
	 * 登录
	 * 
	 * @param name
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("/login.json")
	public Response<String> login(@RequestParam(required = false, name = "name") String name,
	        @RequestParam(required = true, name = "password") String password,
	        HttpServletRequest request) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			List<User> userList = userService.getUserByNamePass(name, password);
			// 设置url
			if (userList.size() > 0) {
				request.getSession().setAttribute("user", userList.get(0));
			} else {
				res = new Response<String>(MessageEnum.ERRORPASS);
			}

		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
		}
		return res;
	}

	/**
	 * 退出
	 * 
	 * @param name
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginout.json")
	public Response<String> loginOut(
	        HttpServletRequest request) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {

			request.getSession().removeAttribute("user");

		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
		}
		return res;
	}

	/**
	 * 测试有没有登录
	 * 
	 * @param name
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("/test.json")
	public Response<String> test() {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		return res;
	}

	@RequestMapping("/synbuyer.json")
	public Response<String> synBuyer(HttpServletRequest request) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			// 旧平台
			// userService.synBuyer();

			// 现在用新平台的
			User user = (User) request.getSession().getAttribute("user");
			userNewService.synBuyer(user.getId());

		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
		}
		return res;
	}

	/**
	 * 顾客列表
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/userlist.json")
	public List<User> userList(@RequestParam(required = false, name = "userId") Integer userId,
	        @RequestParam(required = true, name = "pageNo") Integer pageNo,
	        @RequestParam(required = true, name = "pageSize") Integer pageSize,
	        HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");

			List<User> userList = userService.getUserList(user.getId(), pageNo, pageSize);
			// 设置url

			return userList;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}

	}

	/**
	 * 清除选择
	 * 
	 * @return
	 */
	@RequestMapping("/clearchoice.json")
	public Response<String> clearChoice() {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			userService.clearChoice();
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 挑选用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/choiceusers.json", method = RequestMethod.POST)
	public Response<String> choiceUsers(@RequestBody List<User> checkList, HttpServletResponse response) {

		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			userService.choiceUsers(checkList);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 设置默认
	 * 
	 * @param id
	 * @param nowDefault
	 * @return
	 */
	@RequestMapping("/changedefault.json")
	public Response<String> changeDefault(
	        @RequestParam(required = true, name = "id") Integer id,
	        @RequestParam(required = true, name = "nowDefault") Integer nowDefault) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);

		try {
			userService.changeDefault(id, nowDefault);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 设置默认
	 * 
	 * @param id
	 * @param nowDefault
	 * @return
	 */
	@RequestMapping("/setdefaultuser.json")
	public Response<User> setDefaultuser(@RequestParam(required = false, name = "preUserId") Integer preUserId) {
		Response<User> res = new Response<User>(MessageEnum.SUCCESS);

		try {
			User user = userService.setDefaultuser(preUserId);
			res.setData(user);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<User>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 测试新平台登录
	 * 
	 * @return
	 */
	@RequestMapping("/testconnectnew.json")
	public Response<String> testConnectNew() {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);

		try {
			MessageEnum meEnum = userService.testConnectNew();

			res = new Response<String>(meEnum);
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}
}
