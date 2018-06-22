package com.company.project.service.impl;

import com.company.project.dao.BookmarkClassicMapper;
import com.company.project.model.BookmarkClassic;
import com.company.project.service.BookmarkClassicService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/05/14.
 */
@Service
@Transactional
public class BookmarkClassicServiceImpl extends AbstractService<BookmarkClassic> implements BookmarkClassicService {
    @Resource
    private BookmarkClassicMapper tblBookmarkClassifyMapper;

}
