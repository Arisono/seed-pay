package com.company.project.web.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.company.project.utils.WebSocketUtil;

@ServerEndpoint(value = "/websocket")
@Component 
public class WebSocket {
 
	private static Map<String,Session> sessionPool = new HashMap<String,Session>();
	private Session session;
	
	 @OnOpen
	 public void onOpen(Session session) {
		 session.setMaxTextMessageBufferSize(10 * 1024 * 1024);
	     session.setMaxBinaryMessageBufferSize(10 * 1024 * 1024);
		 this.session=session;
		 System.out.println("设备上线:"+WebSocketUtil.getRemoteAddressStr(session));
		 System.out.println("设备IP："+WebSocketUtil.getRemoteIPAddress(session));
	     sessionPool.put(WebSocketUtil.getRemoteIPAddress(session), session);
	     
	 }
	 
	 @OnClose
	 public void onClose() {
		 System.out.println("设备下线："+WebSocketUtil.getRemoteAddressStr(session));
		 sessionPool.remove(WebSocketUtil.getRemoteIPAddress(session));
	 }
	 
	 @OnMessage
	 public void onMessage(String message, Session session) {
		 System.out.println("设备 "+WebSocketUtil.getRemoteAddressStr(session)+" 发来消息：\n"+message);
	 }
	 
	 @OnError
	 public void onError(Session session, Throwable error) {
	      System.out.println("发生错误:"+WebSocketUtil.getRemoteAddressStr(session));
	      error.printStackTrace();
	 }
	 
	public static synchronized int getOnlineCount() {
		return sessionPool.size();
	}

	
	public synchronized static void sendMessage(String message,String ip){
		Session s = sessionPool.get(ip);
		synchronized(s){
			if(s!=null){
				try {
					System.out.println("服务器向"+ip+"发送消息："+message);
					s.getBasicRemote().sendBinary(ByteBuffer.wrap(message.getBytes()));
					s.getBasicRemote().sendText(message);
			
				} catch (Exception e ) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public synchronized static void setSessionTimeOut(long time,String ip){
		Session s = sessionPool.get(ip);
		s.setMaxIdleTimeout(time);
	}

	
	public static String[] getSessionIP(Map<String, Session> map){
        StringBuilder str=new StringBuilder();
		for (Entry<String, Session> entry : map.entrySet()) {
		   str.append(entry.getKey()+",");
		}
		
        if(StringUtils.isEmpty(str.toString())){
    	  return new String[]{};
        }
		return str.toString().split(",");
	}
	
	public static Map<String, Session> getSessionPool() {
		return sessionPool;
	}
	
}
