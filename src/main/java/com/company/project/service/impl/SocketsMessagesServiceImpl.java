package com.company.project.service.impl;

import com.company.project.dao.SocketsMessagesMapper;
import com.company.project.model.SocketsMessages;
import com.company.project.service.SocketsMessagesService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/20.
 */
@Service
@Transactional
public class SocketsMessagesServiceImpl extends AbstractService<SocketsMessages> implements SocketsMessagesService {
    @Resource
    private SocketsMessagesMapper tblSocketsMessagesMapper;

}
