package com.company.project.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import com.company.project.socket.ServerMain;
import com.jpay.unionpay.SDKConfig;

@Order(1)
public class StartupRunner implements CommandLineRunner {
	@Resource
	ServerMain socketServer;
	private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
	@Override
	public void run(String... args) throws Exception {
		 logger.info("startup runner");
		 //银联加载配置
		 //SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
		
		 //开启socket服务
		 ExecutorService executorService = Executors.newCachedThreadPool();
		 executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				socketServer.startSocketServer();
				
			}
		});
		
	}

}
