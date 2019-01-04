package com.company.project.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import tk.mybatis.mapper.util.StringUtil;


/**
 * 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 */
//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer  {
	@Autowired
	SimpMessagingTemplate template;
	
	private static final Logger log = LoggerFactory
			.getLogger(WebSocketConfig.class);


	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.
        addEndpoint("/")//endpointChat
        .setAllowedOrigins("*")
        .withSockJS();     
	}


	@Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker(
        		"/queue", 
        		"/topic",
        		"/user");	     
//		 .setHeartbeatValue(new long[] {1000,1000})
//	     .setTaskScheduler(new ConcurrentTaskScheduler()) 	            
	      registry.setUserDestinationPrefix("/user");
	    }
	
	
	/** 
     * 输入通道参数设置 
     */  
    @Override  
    public void configureClientInboundChannel(ChannelRegistration registration) {  
        registration.taskExecutor().keepAliveSeconds(60);  
    }


	@Override
	public void configureWebSocketTransport(
			WebSocketTransportRegistration registration) {
		registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
			
			@Override
			public WebSocketHandler decorate(WebSocketHandler handler) {
		
				return  new WebSocketHandlerDecorator(handler){

					@Override
					public void afterConnectionEstablished(
							WebSocketSession session) throws Exception {
						try {			
							log.info("上线-----------------------------------");
							//log.info("客户端name:"+session.getPrincipal().getName());
							log.info("客户端IP:"+session.getRemoteAddress());
						
						} catch (Exception e) {			
							e.printStackTrace();
						}

						super.afterConnectionEstablished(session);
					}

					@Override
					public void handleMessage(WebSocketSession session,
							WebSocketMessage<?> message) throws Exception {
						log.info("handleMessage--------------------------------------------");
                         if(!StringUtil.isEmpty(message.getPayload().toString())){
                        	if (message.getPayload().toString().contains("CONNECT")) {
                        		log.info("发送心跳信息----");
                        		 log.info("getPayload:"+message.getPayload());
							}else{
								log.info("getPayload:"+message.getPayload());
							}
                         }
                       
						super.handleMessage(session, message);
					}

					@Override
					public void afterConnectionClosed(WebSocketSession session,
							CloseStatus closeStatus) throws Exception {
						try {
							log.info("下线--------------------------------------------");
							//log.info("客户端name:"+session.getPrincipal().getName());
							log.info("客户端IP:"+session.getRemoteAddress());
						} catch (Exception e) {
							e.printStackTrace();
						}

						super.afterConnectionClosed(session, closeStatus);
					}
					
				};
						
						
				
			}
		});
		super.configureWebSocketTransport(registration);
	}



	
}
