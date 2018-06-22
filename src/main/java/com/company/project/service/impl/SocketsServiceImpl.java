package com.company.project.service.impl;

import com.company.project.dao.SocketsMapper;
import com.company.project.model.Sockets;
import com.company.project.service.SocketsService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/20.
 */
@Service
@Transactional
public class SocketsServiceImpl extends AbstractService<Sockets> implements SocketsService {
    @Resource
    private SocketsMapper tblSocketsMapper;

}
