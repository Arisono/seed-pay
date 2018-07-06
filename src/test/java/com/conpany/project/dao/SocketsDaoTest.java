package com.conpany.project.dao;

import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.alibaba.fastjson.JSON;
import com.company.project.dao.SocketsMapper;
import com.company.project.dao.UserMapper;
import com.company.project.model.Sockets;
import com.company.project.model.SocketsMessages;
import com.company.project.model.User;
import com.company.project.service.SocketsMessagesService;
import com.company.project.service.SocketsService;
import com.conpany.project.Tester;

public class SocketsDaoTest extends Tester {
	
	@Resource
	private SocketsService socketsService;
	
	@Resource
	private SocketsMessagesService socketsMessagesService;
	
	@Resource
	private  SocketsMapper socketsMapper;
	
	@Resource
	private UserMapper userMapper;
	
	
	/**
	 * 添加一个socket客户端记录
	 */
	@Rollback(false) // 事务自动回滚，默认是true。可以不写
	@Test
	public void addSocketClient(){
		try {
			Sockets s1=new Sockets();
			s1.setSocketIp("192.168.253.200");
			s1.setSocketOnlineTime(new Date());
			s1.setSocketState(1);
			s1.setUserid(1);
			//socketsService.save(s1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加一条socket消息记录
	 */
	@Rollback(false)
	@Test
	public void  addSocketMessages(){
		SocketsMessages smMessages=new SocketsMessages();
		smMessages.setSocketData("你在哪里？好久不见！");
		smMessages.setSocketId("192.168.253.200");
		smMessages.setSocketTime(new Date());
		smMessages.setSocketType(0);
		smMessages.setUserid(1);
		
		socketsMessagesService.save(smMessages);
	
	}
	
	
	/**
	 * 更新上线时间
	 */
	@Rollback(false)
	@Test
	public void updateSocketOnlineTime(){
		//设置主键的情况-----正常更新
//		Sockets s1=new Sockets();
//		s1.setSocketOnlineTime(new Date());
//		s1.setId(2);
//		socketsService.update(s1);
		
		//不设置主键的情况----无法更新
		Sockets s1=	socketsService.findBy("socketIp", "192.168.253.200");
		
//		Sockets s1=new Sockets();
//		s1.setSocketIp("192.168.253.200");
		s1.setSocketOnlineTime(new Date());

		socketsService.update(s1);
	}
	
	
	/**
	 * 更新离线时间
	 */
	@Rollback(false)
	@Test
	public void updateSocketOfflineTime(){
		Sockets s1=new Sockets();
		s1.setSocketOfflineTime(new Date());
		s1.setId(2);
		socketsService.update(s1);
	}
	
	/**
	 * 修改socket连接状态
	 */
	@Rollback(false)
	@Test
	public void updateSocketSate(){
		Sockets s1=new Sockets();
		s1.setSocketState(0);
		s1.setId(2);
		socketsService.update(s1);
	}

	

    /**
     * 根据用户来查询连接记录
     * @throws IOException 
     */
    @Test
    public void selectSocketByUser() throws IOException{
    	
      /**基于注解 演示一对多的映射关系**/
      User userNew=userMapper.findUserWithSockets(1L);
      System.out.println("userNew:"+JSON.toJSONString(userNew));
      
      /**基于注解 演示一对一的映射关系**/
      Sockets socketWithUser=socketsMapper.findSocketsByID(2L);
      System.out.println("socketWithUser:"+JSON.toJSONString(socketWithUser));
      
//    List<User> users=userMapper.findUsers();
//    System.out.println("users:"+JSON.toJSONString(users));
//      
//    User user=  userMapper.findUserWithID(1L);
//    System.out.println("user:"+JSON.toJSONString(user));
//
      
//      List<Sockets> socket= socketsMapper.findSocketsByUserID(1L);
//      System.out.println("socket:"+JSON.toJSONString(socket));
//      
     
//      Sockets socketNew=socketsMapper.findSocketsByID(2L);
//      System.out.println("socketNew:"+JSON.toJSONString(socketNew));
//      
//      List<Sockets> socketNews= socketsService.findAll();
//      System.out.println("socketNews:"+JSON.toJSONString(socketNews));
//      
//      List<Sockets> socketNews2= socketsMapper.findSockets();
//      System.out.println("socketNews2:"+JSON.toJSONString(socketNews2));
    }

}
