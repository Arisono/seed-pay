package com.company.project.web.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.web.socket.WebSocket;

@RestController
@RequestMapping("/websocket")
public class WebSocketController {
	
	@Autowired
    private WebSocket webSocketServer;
	
	@SuppressWarnings("static-access")
	@GetMapping("/send")
	public Result send(@RequestParam String message,@RequestParam String ip){
		try {
			webSocketServer.sendMessage(message, ip);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("发送异常："+e.toString());
		}
		
		return ResultGenerator.genSuccessResult("发送成功！");
	}
	@SuppressWarnings("static-access")
	@GetMapping("/list")
	public Result list(){
		String[] data=webSocketServer.getSessionIP(webSocketServer.getSessionPool());
		return  ResultGenerator.genSuccessResult(data);
	}
	
	@SuppressWarnings("static-access")
	@GetMapping("/setTimeOut")
	public Result setTimeOut(@RequestParam long time,@RequestParam String ip){
		try {
			webSocketServer.setSessionTimeOut(time, ip);
		} catch (Exception e) {
			return  ResultGenerator.genFailResult("发生异常："+e.toString());
		}
		return  ResultGenerator.genSuccessResult("设置成功！");
	}
	
}
