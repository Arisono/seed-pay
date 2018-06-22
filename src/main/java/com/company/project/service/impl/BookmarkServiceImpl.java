package com.company.project.service.impl;

import com.company.project.dao.BookmarkMapper;
import com.company.project.model.Bookmark;
import com.company.project.service.BookmarkService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/05/14.
 */
@Service
@Transactional
public class BookmarkServiceImpl extends AbstractService<Bookmark> implements BookmarkService {
    @Resource
    private BookmarkMapper tblBookmarkMapper;

}
