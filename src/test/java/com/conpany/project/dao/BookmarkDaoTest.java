package com.conpany.project.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.alibaba.fastjson.JSON;
import com.company.project.model.Bookmark;
import com.company.project.model.BookmarkClassic;
import com.company.project.model.User;
import com.company.project.service.BookmarkClassicService;
import com.company.project.service.BookmarkService;
import com.company.project.service.UserService;
import com.conpany.project.Tester;

public class BookmarkDaoTest extends Tester {
	
	@Resource
	UserService userService;
	
	@Resource
	BookmarkClassicService bookmarkclassicService;
	
	@Resource
	BookmarkService bookmarkService;
	
	@Rollback(false) 
	@Test
	public void addUser(){
		User user=new User();
		user.setEmail("78hufdhjid2@qq.com");
		user.setNickName("liujie");
		user.setPassword("123456");
		user.setPhone("13266699268");
		user.setRegisterDate(new Date());
		user.setSex(1);
		user.setUsername("Arison");
		userService.save(user);
	}

	@Rollback(false) 
	@Test
	public void addBookmarkClassic(){
		List<BookmarkClassic> models=new ArrayList<>();
		BookmarkClassic classic1=new BookmarkClassic();
		classic1.setName("常用网址");
		classic1.setTimeCreate(new Date());
		classic1.setUserid(2);
		models.add(classic1);
	
		//bookmarkclassicService.save(models);
	}
	
	
	@Rollback(false) 
	@Test
	public void addBookmark(){
		Bookmark bookmark=new Bookmark();
		bookmark.setClassifyid(36);
		bookmark.setUserid(2);
		bookmark.setName("baidu");
		bookmark.setUrl("https://www.baidu.com/s?wd=mysql%20保留字&rsv_spt=1&rsv_iqid=0xa8ed717400006b7a&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_t=9ec6dOIWYvSw6WcWbm2%2BBQtMf9yKI4TYQoj2sj0%2Bl5WMtQDxSQWlKxxazHCQYFJRe%2FIl");
		bookmark.setDescribe("接口管理平台-UU互联");
		bookmark.setTimeCreate(new Date());
		bookmarkService.save(bookmark);
	}
	
	//级联保存 更新 删除
	@Test
	public void selectData(){
	  //通过多个ID查找
//	  List<Bookmark> datas=	bookmarkService.findByIds("2,eolinker");
//	  System.out.println("datas:"+JSON.toJSON(datas));
	 
	   List<Bookmark> bookmarks=bookmarkService.findAll();
	   System.out.println("书签数据："+JSON.toJSONString(bookmarks));
	   
	   
	   List<BookmarkClassic> bClassics=bookmarkclassicService.findAll();
	   System.out.println("书签分类数据："+JSON.toJSONString(bClassics));
	   
	   List<User> users=userService.findAll();
	   System.out.println("用户数据："+JSON.toJSONString(users));
	}
	
}
