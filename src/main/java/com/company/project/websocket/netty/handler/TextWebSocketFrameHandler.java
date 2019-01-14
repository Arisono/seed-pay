package com.company.project.websocket.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{

	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,TextWebSocketFrame msg) throws Exception {
		     Channel channel = ctx.channel();	     	  
	         String request = ((TextWebSocketFrame) msg).text();
	         System.out.println("服务端收到设备"+channel.remoteAddress()+"的消息"+ request);
		
	}

	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		 System.out.println("与客户端建立连接，通道开启！");
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("与客户端断开连接，通道关闭！");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//super.exceptionCaught(ctx, cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		//super.userEventTriggered(ctx, evt);
	}
	
	
	

}
