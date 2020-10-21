/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年5月7日
 */
package cyyGroup.cyyArt;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @description 异步同步数据配置
 * @author "caiyy"
 * @date 2018年5月7日
 */
@Configuration
// @EnableAsync
public class AsyncConfig {

	@Bean("prepare")
	public Executor taskExecutorQuestion() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(50);
		// 等待时常
		executor.setAwaitTerminationSeconds(60 * 10);
		executor.setThreadNamePrefix("prepare-");
		return executor;
	}

	@Bean("grab")
	public Executor taskExecutorMessage() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(10);
		// 等待时常
		executor.setAwaitTerminationSeconds(60);
		executor.setThreadNamePrefix("grab-");
		return executor;
	}

}
