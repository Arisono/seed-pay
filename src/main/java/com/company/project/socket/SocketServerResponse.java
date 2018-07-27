package com.company.project.socket;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.company.project.model.Sockets;
import com.company.project.service.SocketsMessagesService;
import com.company.project.service.SocketsService;
import com.company.project.utils.BytesUtils;

/**
 * 可以接收,发送socket消息的组件类
 * 
 * @author Arison
 *
 */
@Component
public class SocketServerResponse implements SocketServerResponseInterface {

	private static final Logger log = LoggerFactory.getLogger(SocketServerResponse.class);
	@Resource
	private SocketsService socketsService;// 管理sockets连接状态
	@Resource
	private SocketsMessagesService socketsMessagesService;// 管理sockets消息记录
	
	//数据协议部分
	private String head;
	private String isEncrypt;
	private String deviceNumber;
	


	@Override
	public void clientOnline(String clientIp) {
		log.info(clientIp + " 上线...");
		try {
			System.out.println("-----------------------------------------");
			System.out.println("连接成功！" + clientIp + " time:" + new Date());
			//clientIp 在数据库表中是唯一的，重复插入会报异常。
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
		
		distributeClient(socketResult,clientIp);
		// 这一步，有很多业务操作，非常关键
//		SocketsMessages smMessages = new SocketsMessages();
//		smMessages.setSocketData(socketResult);
//		smMessages.setSocketId(clientIp);
//		smMessages.setSocketTime(new Date());
//		smMessages.setSocketType(0);// 接收数据
//		smMessages.setUserid(1);
//		socketsMessagesService.save(smMessages);

		// 发消息
//		sendMessage(clientIp, socketResult);
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
		System.out.println("IP对象：" + clientIp + " 消息字节："+ Arrays.toString(SocketUtil.hexStringToBytes(msg)));
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

	/**
	 * 业务功能 命令转发 服务器回应客户端指令 7e 00 0f 00 00 08 dc b6 b6 9f 00 07 00 08 dc b6 b6
	 * 9f 72 20-24 是指令类型
	 */
	/**
	 * @param hex
	 * @param clientIP
	 */
	public void distributeClient(String hex,String clientIP) {
		if (!StringUtils.isEmpty(hex)) {
			//头部信息
			 head=hex.substring(0,2);
			//是否加密
			 isEncrypt=hex.substring(6, 8);
			//果蔬机编号
			 deviceNumber=hex.substring(8, 8+6*2);
			 
			//命令类型
			String command = hex.substring(20, 24);
			//计算数据部分
			String length=hex.substring(2, 6);	
			//数据长度  减去9个字节的固定长度
		    int dataLength=BytesUtils.bytesToInt(BytesUtils.hexStringToBytes(length), 0, 2)-9;
			String data=hex.substring(24, 24+2*dataLength);
			
			System.out.println("总字节数："+(dataLength+9+4)+" 机器编号:"+deviceNumber+" 命令类型："+command+" 数据："+data);
			switch (command) {
				case "0001" ://销售订单------回复指令  8001
					//保存销售订单！
					String responseData=data;
					String resCommand="8001";
				
					String result = getResponseCommand( responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "0002" ://机器工作状态------回复指令  8002
					responseData="00"; //成功状态
					resCommand="8002";
				
					result = getResponseCommand(responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "0003" ://机器货物状态------回复指令  8003
					responseData="00"; //成功状态
					resCommand="8003";
				
					result = getResponseCommand(responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "0004" ://用户开锁------回复指令  8004
					
					break;
				case "0005" ://用户结算------回复指令  8005
					responseData="00"; //成功状态
					resCommand="8005";
				
					result = getResponseCommand( responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "0006" ://程序升级服务器发起------回复指令  8006
					responseData="00"; //成功状态
					resCommand="8006";
				
					result = getResponseCommand(responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "0007" ://机器型号  -上线指令------回复指令  8007              
					//第一步：保存设备机器ID
					//第二步：回复指令给客户端  00:不加密 (需要计算校验和)
					responseData="00";
					resCommand="8007";
					
					result = getResponseCommand( responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "0008" ://刷充值卡信息数据------回复指令  8008

					break;
				case "0009" ://礼品货舱------回复指令  8009
					responseData="00";
					resCommand="8009";
					
					result = getResponseCommand(responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "000A" ://请求数据区密------回复指令  800A

					break;
				case "000B" ://门的状态------回复指令  800B
					responseData="00";
					resCommand="800B";
					
					result = getResponseCommand( responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				case "000C" ://用户请求结算------回复指令  800C
					responseData=data;
					resCommand="800B";
					
					result = getResponseCommand(responseData, resCommand);
					System.out.println("result:"+result);
					sendMessage(clientIP, result);
					break;
				default :
					break;
			}
		}

	}
   
	
	


	/**
	 * 公共命令
	 */
	public void actionDevid(String clientIP,String responseData,String resCommand){
		String result = getResponseCommand(responseData, resCommand);
		System.out.println("result:"+result);
		sendMessage(clientIP, result);
	}
	
	/**
	 * 根据发送的数据来封装十六进制字符串格式数据   数据长度 两个字节
	 * @param head
	 * @param isEncrypt
	 * @param deviceNumber
	 * @param responseData
	 * @param resCommand
	 * @return
	 */
	private String getResponseCommand(String responseData, String resCommand) {
		String len=BytesUtils.bytesToHex(BytesUtils.intToBytes((responseData.length()/2+9),2));
		String result=head+len+isEncrypt+deviceNumber+resCommand+responseData;
		String xiaoyanhe=result+"59";
		//计算校验和  一个字节
		String sum=BytesUtils.bytesToHex(BytesUtils.SumCheck(BytesUtils.hexStringToBytes(xiaoyanhe), 1));
		System.out.println("校验和："+sum);
		result=result+sum;
		return result;
	}

}
