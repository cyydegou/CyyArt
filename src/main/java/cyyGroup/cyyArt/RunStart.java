package cyyGroup.cyyArt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
// 开启定时任务
@EnableScheduling
// 开启异步调用方法
@EnableAsync
@MapperScan(basePackages = "cyyGroup.cyyArt.**.dao")
public class RunStart {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		SpringApplication.run(RunStart.class, args);
	}
}
