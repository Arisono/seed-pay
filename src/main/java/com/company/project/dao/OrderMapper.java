package com.company.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.company.project.core.Mapper;
import com.company.project.model.Order;

public interface OrderMapper extends Mapper<Order> {
	
	@Select("Select * from tbl_order where userId= #{userId}")
	List<Order> findOrderByUserId(String userId);
	
	@Select("UPDATE tbl_order SET tradeState = #{tradeState} WHERE orderId = #{orderId}")
	void updateByOrderId(@Param("tradeState") Integer tradeState,@Param("orderId") String orderId);

}