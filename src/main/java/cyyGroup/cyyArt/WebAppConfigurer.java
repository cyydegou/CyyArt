package cyyGroup.cyyArt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cyyGroup.cyyArt.interceptor.ResourceInterceptor;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 登录拦截的管理器
		InterceptorRegistration registration = registry.addInterceptor(ResourceInterceptor()); // 拦截的对象会进入这个类中进行判断
		// 所有路径都被拦截
		registration.addPathPatterns("/**");
		// 添加不拦截路径
		// registration.excludePathPatterns("/", "/login", "/error", "/static/**",
		// "/logout");

	}

	@Bean
	public ResourceInterceptor ResourceInterceptor() {
		return new ResourceInterceptor();
	}
}
