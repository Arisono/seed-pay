package com.company.project.web.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.alibaba.fastjson.JSON;
import com.company.project.dao.DeviceDOMapper;
import com.company.project.dao.OutInRecordDOMapper;
import com.company.project.model.DeviceDO;
import com.company.project.model.OutInRecordDO;
import com.company.project.model.school.Command;
import com.company.project.service.DeviceDOService;
import com.company.project.service.impl.DeviceDOServiceImpl;
import com.company.project.utils.ImgUtil;
import com.company.project.utils.WebSocketUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@SuppressWarnings("unused")
@ServerEndpoint(value = "/websocket" )
@Component
public class WebSocket {
     
	
	//此处是解决无法注入的关键
	private static ApplicationContext applicationContext;
	@Autowired
	public DeviceDOMapper deviceDOMapper;
	@Autowired
	public OutInRecordDOMapper outInRecordDOMapper;
	@Autowired
	public  DeviceDOServiceImpl deviceDOService;
	private static Map<String, Session> sessionPool = new HashMap<String, Session>();
	private Session session;
   
	public WebSocket() {
		super();
	}

	@OnOpen
	public void onOpen(Session session) {
		session.setMaxTextMessageBufferSize(10 * 1024 * 1024);
		session.setMaxBinaryMessageBufferSize(10 * 1024 * 1024);
		this.session = session;
		deviceDOMapper= applicationContext.getBean(DeviceDOMapper.class);
		deviceDOService=applicationContext.getBean(DeviceDOServiceImpl.class);
		outInRecordDOMapper=applicationContext.getBean(OutInRecordDOMapper.class);
		System.out.println("设备上线:" + WebSocketUtil.getRemoteAddressStr(session));	
		sessionPool.put(WebSocketUtil.getRemoteIPAddress(session), session);
	}

	@OnClose
	public void onClose() {
	 System.out.println("设备下线：" + WebSocketUtil.getRemoteAddressStr(session));
	 sessionPool.remove(WebSocketUtil.getRemoteIPAddress(session));
	 
	 DeviceDO dModel=	new DeviceDO();
   	 dModel.setDeviceip(WebSocketUtil.getRemoteIPAddress(session));
   	 DeviceDO fModel= deviceDOMapper.selectOne(dModel);
   	 Condition condition = new Condition(DeviceDO.class);
     Criteria c1 = condition.createCriteria();
   	 if(fModel!=null){
   	     c1.andCondition("serialNumber=",fModel.getSerialnumber());
   		 fModel.setIsonline(0);//下线
   		 fModel.setIsauthorized(0);
   		 deviceDOMapper.updateByCondition(fModel, condition);
   	 }
   	 
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		
		String cmd=JSON.parseObject(message).getString("cmd");
		switch (cmd) {
			case "0" ://设备注册
				System.out.println("来自设备 " + WebSocketUtil.getRemoteAddressStr(session)+ "的消息：\n" + message);
				 sendMessage(new Command<>("6").toJsonStr(), WebSocketUtil.getRemoteIPAddress(session));
				
				 DeviceDO dModel=new DeviceDO();		
			   	 dModel.setSerialnumber(JSON.parseObject(message).getJSONObject("data").getString("serialNumber"));
			   	 DeviceDO fModel= deviceDOMapper.selectOne(dModel);
			   	 Condition condition = new Condition(DeviceDO.class);
			     Criteria c1 = condition.createCriteria();
			   	 if(fModel!=null){
			   	     c1.andCondition("serialNumber=",fModel.getSerialnumber());
			   	     fModel.setDeviceip(WebSocketUtil.getRemoteIPAddress(session));
			   	     fModel.setDevicemac(JSON.parseObject(message).getJSONObject("data").getString("DeviceMac"));
			   	     fModel.setDevicemaker(JSON.parseObject(message).getJSONObject("data").getString("DeviceMaker"));
			   	     fModel.setVersion(JSON.parseObject(message).getJSONObject("data").getString("Version"));
			   	     fModel.setDevicemodel(JSON.parseObject(message).getJSONObject("data").getString("DeviceModel"));
			   		 fModel.setIsonline(1);
			   		 fModel.setIsauthorized(1);
			   		 deviceDOMapper.updateByCondition(fModel, condition);
			   	 }
				break;
			case "1"://白名单列表
				String identity=JSON.parseObject(message).getJSONObject("data").getString("identity");
				String name=JSON.parseObject(message).getJSONObject("data").getString("name");
				System.out.println("identity:"+identity+"  name:"+name);
				break;
			case "19"://进出入校通知
				System.out.println("来自设备 " + WebSocketUtil.getRemoteAddressStr(session)+ "的消息：\n" + message);
				 identity=JSON.parseObject(message).getJSONObject("data").getString("identity");
				 name=JSON.parseObject(message).getJSONObject("data").getString("name");
				 String photo=JSON.parseObject(message).getJSONObject("data").getString("photo");
				 String time=JSON.parseObject(message).getJSONObject("data").getString("time");
				 String serialNumber=JSON.parseObject(message).getJSONObject("data").getString("serialNumber");
		    	 OutInRecordDO outModel=new OutInRecordDO();
		    	 //DateUtils.parseDate("2019-08-12 08:00:12", "yyyy-MM-dd HH:mm:ss")
				 outModel.setInDate(new Date(Long.valueOf(time)));
				 ImgUtil.baseStrToImg(photo, "C://Users//Arison//Desktop//upload//liuie.jpg");
		    	 outModel.setRecordDetails("C://Users//Arison//Desktop//upload//liuie.jpg");
		    	 outModel.setRecordName("进校记录");
		    	 outModel.setStuId(Long.valueOf(identity));
		    	 outInRecordDOMapper.insert(outModel);
				break;
			case "11"://设备所有的识别记录
				
				
				break;
			case "100":
                //{"cmd":"100","data":{"op":"3","serialNumber":"3100-1d17-72be-ed51-cac9","status":"ok"}}
				String op=JSON.parseObject(message).getJSONObject("data").getString("op");
			    String status=JSON.parseObject(message).getJSONObject("data").getString("status");
			    switch (op) {
					case "3" ://上传白名单设备响应
						if ("ok".equals(status)) {
							//更新学生表状态
							
							
						}
						break;

					default :
						break;
				}
				break;
			default :
				System.out.println("来自设备 " + WebSocketUtil.getRemoteAddressStr(session)+ "的消息：\n" + message);
				break;
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误:" + WebSocketUtil.getRemoteAddressStr(session));
	    error.printStackTrace();
	}

	public static synchronized int getOnlineCount() {
		return sessionPool.size();
	}

	public synchronized static void sendMessage(String message, String ip) {
		Session s = sessionPool.get(ip);
		synchronized (s) {
			if (s != null) {
				try {
					System.out.println("服务器向" + ip + "发送消息：" + message);
//					s.getBasicRemote()
//							.sendBinary(ByteBuffer.wrap(message.getBytes()));
					s.getBasicRemote().sendText(message);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public synchronized static  void close( String ip){
		Session s = sessionPool.get(ip);
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void setSessionTimeOut(long time, String ip) {
		Session s = sessionPool.get(ip);
		s.setMaxIdleTimeout(time);
	}

	public static String[] getSessionIP(Map<String, Session> map) {
		StringBuilder str = new StringBuilder();
		for (Entry<String, Session> entry : map.entrySet()) {
			str.append(entry.getKey() + ",");
		}

		if (StringUtils.isEmpty(str.toString())) {
			return new String[]{};
		}
		return str.toString().split(",");
	}

	public static Map<String, Session> getSessionPool() {
		return sessionPool;
	}

	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		WebSocket.applicationContext = applicationContext;
	}

	
}
