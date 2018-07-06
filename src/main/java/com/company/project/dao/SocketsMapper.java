package com.company.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.company.project.core.Mapper;
import com.company.project.model.Sockets;

public interface SocketsMapper extends Mapper<Sockets> {
	
	@Select("SELECT id as id ,"
			+ "socket_ip  as socketIp ,"
			+ " socket_online_time as  socketOnlineTime ,"
			+ " socket_offline_time  as socketOfflineTime ,"
			+ " socket_state as  socketState ,"
			+ " userid  as userId "
			+ "FROM `tbl_sockets` where userid= #{id}")
	List<Sockets> findSocketsByUserID(Long id);
	
	@Select("SELECT id as id ,"
			+ "socket_ip  as socketIp ,"
			+ " socket_online_time as  socketOnlineTime ,"
			+ " socket_offline_time  as socketOfflineTime ,"
			+ " socket_state as  socketState ,"
			+ "userid  as userId "
			+ "FROM `tbl_sockets` where id= #{id}")
	 @Results({
         @Result(property = "user", column = "userid",
                 one =@One(select = "com.company.project.dao.UserMapper.findUserWithID"))
     })
	Sockets findSocketsByID(Long id);

	@Select("SELECT id as id ,"
			+ "socket_ip  as socketIp ,"
			+ " socket_online_time as  socketOnlineTime ,"
			+ " socket_offline_time  as socketOfflineTime ,"
			+ " socket_state as  socketState ,"
			+ "userid  as userId "
			+ "FROM  tbl_sockets  where 1=1")
	List<Sockets> findSockets();
}