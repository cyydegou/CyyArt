package cyyGroup.cyyArt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.vo.Response;

public class ResourceInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 在请求处理之前进行调用（Controller方法调用之前） 基于URL实现的拦截器
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		// 只拦截接口
		String contextPath = request.getServletPath();
		if (!contextPath.contains(".json") || contextPath.contains("login.json")
		        || contextPath.contains("testsyn.json")) {
			return true;
		}
		Object user = request.getSession().getAttribute("user");
		if (user == null) {
			// 不需要的拦截直接过
			Response<String> res = new Response<String>(MessageEnum.NOTLOGIN);
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(JSONObject.toJSONString(res));
			return false;
		}
		return true;

	}
}
