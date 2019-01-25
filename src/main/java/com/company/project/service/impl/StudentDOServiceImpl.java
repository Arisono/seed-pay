package com.company.project.service.impl;

import com.company.project.dao.StudentDOMapper;
import com.company.project.model.StudentDO;
import com.company.project.service.StudentDOService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/18.
 */
@Service
@Transactional
public class StudentDOServiceImpl extends AbstractService<StudentDO> implements StudentDOService {
    @Resource
    private StudentDOMapper sysStudentMapper;

}
