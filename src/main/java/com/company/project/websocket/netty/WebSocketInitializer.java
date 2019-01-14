package com.company.project.websocket.netty;

import com.company.project.websocket.netty.handler.TextWebSocketFrameHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketInitializer extends ChannelInitializer<SocketChannel>{

	EventLoopGroup busiGroup;

    public WebSocketInitializer(EventLoopGroup busiGroup) {
        this.busiGroup = busiGroup;
    }
	
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast("logging", new LoggingHandler(LogLevel.ERROR));
		socketChannel.pipeline().addLast("http-codec",new HttpServerCodec());
		socketChannel.pipeline().addLast("aggregator", new HttpObjectAggregator(65535 * 6));
	    socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
	    socketChannel.pipeline().addLast("websocket", new WebSocketServerProtocolHandler("/websocket", null, false, 65535 * 6, false));
	    socketChannel.pipeline().addLast(busiGroup, "TextWebSocket", new TextWebSocketFrameHandler());		
	}

}
