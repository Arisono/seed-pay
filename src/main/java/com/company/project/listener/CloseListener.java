package com.company.project.listener;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.company.project.dao.DeviceDOMapper;
import com.company.project.model.DeviceDO;
import com.company.project.socket.ServerMain;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Component
public class CloseListener  implements ApplicationListener<ContextClosedEvent>{
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired 
    ServerMain socketServer;
    @Resource
	public DeviceDOMapper deviceDOMapper;
    
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("程序停止");
        
        //停止socket
        socketServer.closeSocket();
        logger.info("关闭socket服务器");
        
        //所有设备断开连接并更新状态
        List<DeviceDO> devices=   deviceDOMapper.selectAll();
        Condition condition = new Condition(DeviceDO.class);
        Criteria c1 = condition.createCriteria();
        for (int i = 0; i < devices.size(); i++) {
        	DeviceDO model=	devices.get(i);
        	model.setIsonline(0);
        	model.setIsauthorized(0);
        	model.setIsautomatic(0);
        	c1.andCondition("serialNumber=",model.getSerialnumber());
        	deviceDOMapper.updateByCondition(model, condition);
		}
    }
}
