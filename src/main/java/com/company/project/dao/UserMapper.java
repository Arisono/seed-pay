package com.company.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.company.project.core.Mapper;
import com.company.project.model.User;

public interface UserMapper extends Mapper<User> {
	
	    @Select("SELECT * FROM `tbl_user` where 1=1")
	    List<User>  findUsers();
	
	    @Select("SELECT * FROM `tbl_user` where id = #{id}")
	    User findUserWithID(Long id);
	    
	    @Select("SELECT * FROM `tbl_user` where id = #{id}")
	    @Results({
            @Result(property = "sockets", column = "id",
                    many =@Many(select = "com.company.project.dao.SocketsMapper.findSocketsByUserID"))
        })
	    User findUserWithSockets(Long id);
	    
	    
	    
}