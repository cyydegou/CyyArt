/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年5月7日
 */
package cyyGroup.cyyArt.task;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cyyGroup.cyyArt.money.service.GrabService;
import cyyGroup.cyyArt.money.service.OrderService;

/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年5月7日
 */
@Component
@Async
public class GrabTask {

	@Autowired
	private GrabService grabService;

	@Autowired
	private OrderService orderService;

	@Resource(name = "prepare")
	private Executor executorPrepare;

	@Resource(name = "grab")
	private Executor executorGrab;

	private Log logger = LogFactory.getLog(getClass());

	@Scheduled(cron = "0/5 * * * * *")
	public void prepareGrab() throws IOException, InterruptedException {
		executor(1, null);
	}

	// 保持活力的，不需要了
	// @Scheduled(cron = "0 0 */4 * * ?")
	// public void keepAlive() throws IOException, InterruptedException {
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// logger.info("keepAlive:" + format.format(new Date()));
	// executor(3, null);
	// }

	class Task implements Callable<Object> {
		private Integer type;
		private Object obj;

		public Task(Integer type, Object obj) {
			this.type = type;
			this.obj = obj;
		}

		@Override
		public Object call() throws Exception {
			if (type == 1) {
				grabService.prepareGrab();
			} else if (type == 2) {
				grabService.doGrab(obj);
			} else if (type == 3) {
				orderService.keepAlive();
			} else if (type == 4) {
				grabService.doGrabNew(obj);
			}
			return null;
		}
	}

	public void executor(Integer type, Object obj) {
		Task t = new Task(type, obj);
		FutureTask<Object> futureTask = new FutureTask<Object>(t);
		if (type == 1) {
			executorPrepare.execute(futureTask);
		} else if (type == 2) {
			executorGrab.execute(futureTask);
		} else if (type == 3) {
			executorPrepare.execute(futureTask);
		} else if (type == 4) {
			executorGrab.execute(futureTask);
		}

		// 在这里可以做别的任何事情
		try {
			futureTask.get(1000 * 60 * 60, TimeUnit.MILLISECONDS);
			// 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
		} catch (InterruptedException e) {
			logger.error("---------InterruptedException--------", e);
			futureTask.cancel(true);
		} catch (ExecutionException e) {
			logger.error("---------ExecutionException----------", e);
			futureTask.cancel(true);
		} catch (TimeoutException e) {
			logger.error("---------Time out-------------", e);
			futureTask.cancel(true); // 取消执行的线程
		}

	}

}
