package com.company.project.web.socket;

import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.project.model.Message;
import com.company.project.model.Response;
import com.company.project.model.ToUserMessage;

//@SuppressWarnings("deprecation")
//@Controller
//@ServerEndpoint("/")  
public class WebSocketStompController {
	@Autowired
	SimpMessagingTemplate template;
	
	@OnOpen
	public void openSuccess(){
		System.out.println("客户端打开连接！-----------------");
	}

	@MessageMapping("/connectioned")
	@SendTo("/topic/getResponse")
	public Response connectionSuccess() {
		return new Response("作者在线：您需要帮助吗？");
	}

	/**
	 * 广播消息
	 */
	@MessageMapping("/welcome") 
	@SendTo("/topic/getResponse")
	public Response say(Message message) throws Exception {
		Thread.sleep(1000);
		return new Response("Welcome, " + message.getName() + "!");
	}
	
	/**
	 * 广播消息
	 */
	@ResponseBody
	@GetMapping("/topic")
	public void sendMessage() {
		template.convertAndSend("/topic/getResponse", "广播消息：topic主题");
	}

	@MessageMapping("/cheat")
	// 发送的订阅路径为/user/{userId}/message
	public void cheatTo(ToUserMessage toUserMessage) {
		// 方法用于点对点测试
		// System.out.println(toUserMessage.getUserId());
		// System.out.println("p:"+p.getName());
		template.convertAndSendToUser("13266699268", "/message",toUserMessage.getMessage());
	}
}
