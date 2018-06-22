package com.conpany.project.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.alibaba.fastjson.JSON;
import com.company.project.dao.OrderMapper;
import com.company.project.model.AliPayBean;
import com.company.project.model.Order;
import com.company.project.model.WxPayBean;
import com.company.project.service.OrderService;
import com.conpany.project.Tester;

import junit.framework.TestCase;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

public class OrderDaoTest extends Tester {

	@Resource
	private OrderService orderService;
	@Resource
	private OrderMapper tblOrderMapper;
	@Resource
	private AliPayBean aliPayBean;
	@Resource
	private WxPayBean wxPayBean;
	@Test
	public void testConfig() {
		TestCase.assertEquals(aliPayBean.getAppId(), "2017110609768534");
		TestCase.assertEquals(wxPayBean.getAppId(), "wxd1deafafe3fd9a21");
	}

	@Test
	public void testfindByIds() {
		List<Order> orders = orderService.findByIds("76,050917392215258");

		System.out.println("orders:" + JSON.toJSONString(orders));
	}
	@Test
	public void testCondition() {
		Condition condition = new Condition(Order.class);

		Criteria c1 = condition.createCriteria();
		c1.orCondition("tradeState=", "0");
		// c1.orCondition("tradeState=", "1");
		// c1.orCondition("tradeState=", "2");

		Criteria c2 = condition.createCriteria();
		c2.andCondition("userId=", "13266699268");
		condition.and(c2);

		List<Order> orders = orderService.findByCondition(condition);
		System.out.println("Arison:" + JSON.toJSONString(orders));
		//TestCase.assertEquals(orders.size(), 1);
		//TestCase.assertEquals(orders.get(0).getOrderid(), "050814184115257");
	}

	@Rollback(false) // 事务自动回滚，默认是true。可以不写
	@Test
	public void testUpdate() {
		Order order = new Order();
		order.setOrderid("050914513315258");
		order.setTransactionid("4200000129201805095973917744");
		order.setTradestate(1);
		order.setTimePayment(new Date());
		// update 通用mapper是采用一个主键查询的方式，目前不支持多主键联合查询
		orderService.update(order);
		// tblOrderMapper.updateByOrderId(2, "050814184115257");
	}

	@Rollback(false) // 事务自动回滚，默认是true。可以不写
	@Test
	public void testAdd() {
		Order order = new Order();
		order.setOrderid("123343");
		order.setTransactionid("123");
		order.setUserid("132666");
		order.setTradestate(2);
		order.setFee(Float.valueOf("0.07"));
		order.setTimeCreate(new Date());
		//orderService.save(order);
	}

	@Rollback(false)
	@Test
	public void testMapper() { // 自己实现的sql语句查询
		List<Order> datas = tblOrderMapper.findOrderByUserId("13266699268");
		//TestCase.assertEquals(datas.isEmpty(), false);
	}

	@Rollback(false)
	@Test
	public void testSelectMapper() {
		Order order = new Order();
		order.setUserid("132-66699268");
		List<Order> datas = tblOrderMapper.select(order);
		//TestCase.assertEquals(datas.size(), 1);
		TestCase.assertEquals(datas.get(0).getUserid(), "132-66699268");
	}
}
