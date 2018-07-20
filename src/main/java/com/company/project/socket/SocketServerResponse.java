package com.company.project.socket;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.company.project.model.Sockets;
import com.company.project.model.SocketsMessages;
import com.company.project.service.SocketsMessagesService;
import com.company.project.service.SocketsService;

/**
 * 可以接收,发送socket消息的组件类
 * 
 * @author Arison
 *
 */
@Component
public class SocketServerResponse implements SocketServerResponseInterface {

	private static final Logger log = LoggerFactory
			.getLogger(SocketServerResponse.class);
	@Resource
	private SocketsService socketsService;// 管理sockets连接状态
	@Resource
	private SocketsMessagesService socketsMessagesService;// 管理sockets消息记录

	@Override
	public void clientOnline(String clientIp) {
		log.info(clientIp + " 上线...");
		try {
			System.out.println("-----------------------------------------");
			System.out.println("连接成功！" + clientIp + " time:" + new Date());
			// clientIp 在数据库表中是唯一的，重复插入会报异常。
			Sockets s0 = socketsService.findBy("socketIp", clientIp);
			if (s0 != null) {
				// 更新上线时间
				s0.setSocketOnlineTime(new Date());
				s0.setSocketState(1);// 上线状态
				socketsService.update(s0);
			} else {
				// 保存客户端连接
				Sockets s1 = new Sockets();
				s1.setSocketIp(clientIp);
				s1.setSocketOnlineTime(new Date());
				s1.setSocketState(1);// 上线状态
				s1.setUserid(1);// 绑定系统用户
				System.out.println("socketsService:" + socketsService);
				socketsService.save(s1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSocketReceive(String socketResult, String clientIp) {
		log.info(clientIp + " 发来消息...");
		log.info("消息：" + socketResult);
		// 这一步，有很多业务操作，非常关键
		SocketsMessages smMessages = new SocketsMessages();
		smMessages.setSocketData(socketResult);
		smMessages.setSocketId(clientIp);
		smMessages.setSocketTime(new Date());
		smMessages.setSocketType(0);// 接收数据
		smMessages.setUserid(1);
		socketsMessagesService.save(smMessages);
		
		//发消息
		//byte [] hui={0x7e,0x00,0x0f,0x00,0x00,0x08,(byte) 0xdc,(byte) 0xb6,(byte) 0xb6,(byte) 0x9f,0x1f,0x47,0x00,(byte) 0xe2};   
		//String hui="AE000F000008DCA9A9A200070008DCA9A9A2CD";
	    sendMessage(clientIp, socketResult);
	}

	@Override
	public void clientOffline(String clientIp) {
		log.info(clientIp + " 下线...");
		Sockets s1 = socketsService.findBy("socketIp", clientIp);
		s1.setSocketOfflineTime(new Date());// 离线时间
		s1.setSocketState(0);// 离线状态
		socketsService.update(s1);
	}

	/**
	 * 服务器发送消息给指定客户端
	 * 
	 * @param clientIp
	 * @param msg
	 */
	public void sendMessage(String clientIp, String msg) {
		System.out.println("IP对象："+clientIp+" 消息字节："+Arrays.toString(SocketUtil.hexStringToBytes(msg)));
		ConcurrentCache.getCache(clientIp).addMessage(msg);
	}

	/**
	 * 服务器主动断线
	 * 
	 * @param clientIp
	 */
	public void stopSocket(String clientIp) {
		ConcurrentCache.getCache(clientIp).stop();
	}

	/**
	 * 给所有客户端发送消息
	 * 
	 * @param msg
	 */
	public void sendMessageToAllClient(String msg) {
		try {
			ConcurrentHashMap<String, ServerResponseThread> serverManager = ConcurrentCache
					.getServerManager();
			for (Map.Entry<String, ServerResponseThread> entry : serverManager
					.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = "
						+ entry.getValue());
				sendMessage(entry.getKey(), msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
