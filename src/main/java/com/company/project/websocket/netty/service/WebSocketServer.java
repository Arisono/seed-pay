package com.company.project.websocket.netty.service;

import com.company.project.websocket.netty.WebSocketInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketServer {
	
	private static int port =10089;//默认端口

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        EventLoopGroup busiGroup = new NioEventLoopGroup();//业务处理线程
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)              
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, 
                    		new FixedRecvByteBufAllocator(592048))
                    .childHandler(new WebSocketInitializer(busiGroup));
            System.out.println("Websocket server start on port:"+ port);
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅的退出程序
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
