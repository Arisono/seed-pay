package com.company.project.service.impl;

import com.company.project.dao.DeviceDOMapper;
import com.company.project.model.DeviceDO;
import com.company.project.service.DeviceDOService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/18.
 */
@Service
@Transactional
public class DeviceDOServiceImpl extends AbstractService<DeviceDO> implements DeviceDOService {
    @Resource
    private DeviceDOMapper deviceMapper;

}
