package com.company.project.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.company.project.socket.ServerMain;

@Component
public class CloseListener  implements ApplicationListener<ContextClosedEvent>{
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired ServerMain socketServer;
    
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("程序停止");
        
        socketServer.closeSocket();
        logger.info("关闭socket服务器");
    }
}
