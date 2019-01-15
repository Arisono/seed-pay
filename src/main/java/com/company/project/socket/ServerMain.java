package com.company.project.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 原生socket服务
 * @author Arison
 *
 */
@Component
public class ServerMain {

	private static final Logger log = LoggerFactory.getLogger(ServerMain.class);
	private boolean isStart = true;
	private ServerSocket serverSocket = null;
	@Resource
	private SocketServerResponse serverResponse;
	private ServerResponseThread serverResponseThread;
	
	/**
	 * 启动指定端口号的socket服务
	 */
	public void startSocketServer(){
	    ExecutorService executorService = Executors.newCachedThreadPool();
	    log.info("服务端 " + SocketUtil.getIP() +":"+SocketUtil.PORT+  " 运行中...\n");
	    try {
	    	   serverSocket = new ServerSocket(SocketUtil.PORT);
	            while (isStart) {
	                Socket socket = serverSocket.accept();
	                socket.setSoTimeout(600000);//设定输入流读取阻塞超时时间(600秒收不到客户端消息判定断线)
	                serverResponseThread = new ServerResponseThread(socket,serverResponse);  //新开一个线程处理，发送，接收数据
	                if (socket.isConnected()) {
	                    executorService.execute(serverResponseThread);
	                    ConcurrentCache.put(serverResponseThread.getUserIP(), serverResponseThread);
	                }
	            }
//	            if(!serverSocket.isClosed()){
//		        	serverSocket.close();	
//		        }	
	            System.out.println("socket服务器关闭！");
	        } catch (IOException e) {
//	            e.printStackTrace();
	        } finally {
	            closeSocket();
	        }

	}

	public void closeSocket() {
		if (serverSocket != null) {
		    try {
		        isStart = false;//关闭socket服务
		        if(!serverSocket.isClosed()){
		        	serverSocket.close();	
		        }		      
		        if(serverResponseThread!=null){
		        	 serverResponseThread.stop();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}

	
}
