package com.company.project.service.impl;

import com.company.project.dao.OutInRecordDOMapper;
import com.company.project.model.OutInRecordDO;
import com.company.project.service.OutInRecordDOService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/18.
 */
@Service
@Transactional
public class OutInRecordDOServiceImpl extends AbstractService<OutInRecordDO> implements OutInRecordDOService {
    @Resource
    private OutInRecordDOMapper outInRecordMapper;

}
