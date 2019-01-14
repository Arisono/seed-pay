package com.company.project.web.websocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	@PostMapping("/send")
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
	
	@GetMapping("/setUdp")
	public Result sendUdp(@RequestParam(required = false) String message){		
	    String host = "255.255.255.255";
        int port = 10085;
        if (StringUtils.isEmpty(message)) {
        	  message = "10.1.80.196:8085";
		}       
        try {
            InetAddress adds = InetAddress.getByName(host);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(message.getBytes(),
            message.length(), adds, port);
            ds.send(dp);
            return  ResultGenerator.genSuccessResult("发送成功！");
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
		return null;
	}
	
}
