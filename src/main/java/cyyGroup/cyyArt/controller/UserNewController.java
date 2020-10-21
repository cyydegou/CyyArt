package cyyGroup.cyyArt.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.money.service.UserNewService;
import cyyGroup.cyyArt.until.CyyArtException;
import cyyGroup.cyyArt.vo.Response;
import cyyGroup.cyyArt.vo.User;

@RestController
@RequestMapping("/user/new")
public class UserNewController {

	@Autowired
	private UserNewService userNewService;

	private Log logger = LogFactory.getLog(getClass());

	/**
	 * 测试逆向同步
	 * 
	 * @param id
	 * @param nowDefault
	 * @return
	 */
	@RequestMapping("/testsyn.json")
	public Response<String> testSyn() {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);

		try {
			userNewService.testSyn();
			return res;
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
			return res;
		}

	}

	/**
	 * 登录
	 * 
	 * @param name
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginbai.json")
	public Response<String> loginBai(@RequestParam(required = false, name = "name") String name,
	        @RequestParam(required = true, name = "password") String password,
	        HttpServletRequest request) {
		Response<String> res = new Response<String>(MessageEnum.SUCCESS);
		try {
			User user = (User) request.getSession().getAttribute("user");

			MessageEnum resStatus = userNewService.loginBai(user.getId(), name, password);
			// 设置url
			if (resStatus != null) {
				res = new Response<String>(resStatus);
			} else {
				res = new Response<String>(MessageEnum.FAIL);
			}

		} catch (CyyArtException e) {
			res = new Response<String>(MessageEnum.FAIL);
			res.setMessage(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			res = new Response<String>(MessageEnum.FAIL);
		}
		return res;
	}
}
