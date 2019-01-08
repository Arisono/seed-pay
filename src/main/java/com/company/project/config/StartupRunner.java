package com.company.project.config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import com.company.project.socket.ServerMain;
import com.jpay.unionpay.SDKConfig;

@SuppressWarnings("unused")
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
		 startSocket();
		 //发送udp消息
		 sendUdpMeassage();		
	}


	private void startSocket() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				
				socketServer.startSocketServer();
				
			}
		});
	}
	
	
	@SuppressWarnings("resource")
	private void sendUdpMeassage() {
		    String host = "255.255.255.255";
	        int port = 10085;
	        String message = "10.1.80.196:8085";
	        try {
	            InetAddress adds = InetAddress.getByName(host);
	            DatagramSocket ds = new DatagramSocket();
	            DatagramPacket dp = new DatagramPacket(message.getBytes(),
	            message.length(), adds, port);
	            ds.send(dp);
	        }
	        catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
	        catch (SocketException e) {
	            e.printStackTrace();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	}

}
