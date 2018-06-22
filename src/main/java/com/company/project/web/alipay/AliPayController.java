package com.company.project.web.alipay;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.company.project.model.AliPayBean;
import com.company.project.model.Order;
import com.company.project.service.OrderService;
import com.jpay.alipay.AliPayApi;
import com.jpay.alipay.AliPayApiConfig;
import com.jpay.alipay.AliPayApiConfigKit;
import com.jpay.util.StringUtils;
import com.jpay.vo.AjaxResult;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Controller
@RequestMapping("/alipay")
public class AliPayController extends AliPayApiController {
	
	@Resource
	private OrderService orderService;
	@Autowired
	private AliPayBean aliPayBean;
	private AjaxResult result = new AjaxResult();
	
	@Override
	public AliPayApiConfig getApiConfig() {
		return AliPayApiConfig.New()
				.setAppId(aliPayBean.getAppId())
				.setAlipayPublicKey(aliPayBean.getPublicKey())
				.setCharset("UTF-8")
				.setPrivateKey(aliPayBean.getPrivateKey())
				.setServiceUrl(aliPayBean.getServerUrl())
				.setSignType("RSA2")
				.build();
	}
	
	@RequestMapping("")
	@ResponseBody
	public String index() {
		return "欢迎使用支付宝支付测试接口";
	}
	


	/**
	 * App支付
	 * @param userId 用户ID
	 * @param totalAmount  支付金额
	 * @param outTradeNo 订单号 选填
	 * @param body 订单内容
	 * @param subject 订单主题
	 * @return
	 */
	@RequestMapping(value = "/appPay")
	@ResponseBody
	public AjaxResult appPay(@RequestParam("userId")String userId,
			@RequestParam("totalAmount") String totalAmount,
			@RequestParam(name="out_trade_no",required=false) String outTradeNo,
			@RequestParam(value="body",required=false,defaultValue="body") String body,
			@RequestParam(value="subject",required=false,defaultValue="body") String subject) {
		try {
			
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody(body);
			model.setSubject(subject);
			String orderId="";
			if(StringUtils.isEmpty(outTradeNo)){
			    orderId=StringUtils.getOutTradeNo();
			}else{
				orderId=outTradeNo;
			}
			model.setOutTradeNo(orderId);
			model.setTimeoutExpress("30m");
			model.setTotalAmount(totalAmount);//单位 元
			model.setPassbackParams("callback params");
			model.setProductCode("QUICK_MSECURITY_PAY");
		
			String orderInfo = AliPayApi.startAppPay(model, aliPayBean.getDomain() + "/alipay/notify_url");
			result.success(orderInfo);
			
			System.out.println(""+AliPayApiConfigKit.getAliPayApiConfig().getServiceUrl());
			Order order=new Order();
			order.setBody(body);
			order.setDetail(subject);
			order.setPaytype("支付宝");
			order.setFee(Float.valueOf(totalAmount));
			order.setOrderid(orderId);
			order.setTimeCreate(new Date());
			order.setTradestate(0);//未支付
			order.setUserid(userId);
			if (StringUtils.isEmpty(outTradeNo)) {
				orderService.save(order);
			}else{
				orderService.update(order);
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			result.addError("system error:"+e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 退款
	 */
	@RequestMapping(value = "/tradeRefund")
	@ResponseBody
	public String tradeRefund(
			@RequestParam("outTradeNo") String OutTradeNo,
			@RequestParam(value="tradeNo",required=false) String TradeNo,
			@RequestParam("refundAmount") String RefundAmount,
			@RequestParam(value="refundReason",required=false) String RefundReason) {
		try {
			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(OutTradeNo);//商户订单号		 
			model.setTradeNo(TradeNo);//支付宝订单号
			model.setRefundAmount(RefundAmount);
			model.setRefundReason(RefundReason);
			
			
			Order order=new Order();
			order.setOrderid(OutTradeNo);
			order.setTransactionid(TradeNo);
			order.setTradestate(2);
			order.setRefundfee(Double.valueOf(RefundAmount));
			orderService.update(order);
			
	  
			//支付宝退款第一次走回调，与支付回调走相同接口
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			request.setBizModel(model);
			request.setNotifyUrl(aliPayBean.getDomain() + "/alipay/refund_url");//退款回调
			AlipayTradeRefundResponse response= AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
			return response.getBody();
			//return AliPayApi.tradeRefund(model);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 查询订单列表
	 * @param userId
	 * @param tradeState
	 * @return
	 */
	@RequestMapping(value = "/orderquery")
	@ResponseBody
	public JSONArray orderquery(@RequestParam("userId") String userId,
			@RequestParam(value="tradeState",defaultValue = "") String tradeState){
		    Condition condition = new Condition(Order.class);
			Criteria c1=condition.createCriteria();
			if ("0".equals(tradeState)) {
				c1.orCondition("tradeState=", "0");
			}
			if ("1".equals(tradeState)) {
				c1.orCondition("tradeState=", "1");		
			}
			if ("2".equals(tradeState)) {
				c1.orCondition("tradeState=", "2");
			}
			if (StringUtils.isEmpty(tradeState)) {
				c1.orCondition("tradeState=", "0");
				c1.orCondition("tradeState=", "1");
				c1.orCondition("tradeState=", "2");
			}
			
			Criteria c2=condition.createCriteria();
			c2.andCondition("userId=", "13266699268");
			condition.and(c2);
			
			Criteria c3=condition.createCriteria();
			
			c3.andCondition("payType=", "支付宝");
			condition.and(c3);
		
		    List<Order> orders= orderService.findByCondition(condition);
		    JSONArray json = new JSONArray();
			for(Order order : orders){
				JSONObject jo = new JSONObject();
				jo.put("outTradeNo", order.getOrderid());
				jo.put("trade_no",order.getTransactionid());
				jo.put("tradeState",order.getTradestate());
				jo.put("timeExpire",order.getTimePayment());
				jo.put("fee",order.getFee());
				jo.put("timeStart",order.getTimeCreate());
				jo.put("outRefundNo",order.getOrderrefundid());
				jo.put("refundSuccessTime",order.getTimeRefund());
				json.add(jo);
			}
		    
		    result.setData(json);
		return json;
	}
	
	@RequestMapping(value = "/notify_url")
	@ResponseBody
	public String  notify_url(HttpServletRequest request) {
		try {
			System.out.println("++++++++++++++支付宝  支付回调 ++++++++++++++++++++++++++");
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(request);
             System.out.println("支付宝  params:"+JSON.toJSONString(params));
			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}
			boolean verify_result = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8",
					"RSA2");

			if (verify_result) {
				System.out.println("notify_url 验证成功succcess");
				String trade_no = params.get("trade_no");
				String trade_status=params.get("trade_status");
				String out_trade_no      = params.get("out_trade_no");
				
				if ("TRADE_SUCCESS".equals(trade_status)) {
					// 商户订单号
					System.out.println("支付成功:"+JSON.toJSONString(params));
					Order order=new Order();
					order.setOrderid(out_trade_no);
					order.setTransactionid(trade_no);
					order.setTimePayment(new Date());
					order.setTradestate(1);//已支付
					orderService.update(order);
				}else if("TRADE_CLOSED".equals(trade_status)){
					Order order=new Order();
					order.setOrderid(out_trade_no);
					order.setTransactionid(trade_no);
					order.setTimeRefund(new Date());
					order.setTradestate(2);//已退款
					orderService.update(order);
				}
				return "success";
			} else {
				System.out.println("notify_url 验证失败");
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}
	
	
	/**
	 * 支付宝退款不走这个接口
	 * @param request
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/refund_url")
	@ResponseBody
	public String  refund_url(HttpServletRequest request) {
		try {
			System.out.println("++++++++++++++支付宝  退款回调 ++++++++++++++++++++++++++");
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(request);
             System.out.println("支付宝  params:"+JSON.toJSONString(params));
			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}
			boolean verify_result = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8",
					"RSA2");

			if (verify_result) {
				System.out.println("notify_url 验证成功succcess");
				String trade_no = params.get("trade_no");
				// 商户订单号
				String out_trade_no      = params.get("out_trade_no");
				System.out.println("支付成功:"+JSON.toJSONString(params));
				Order order=new Order();
				order.setOrderid(out_trade_no);
				order.setTransactionid(trade_no);
				order.setTradestate(2);//已退款
				orderService.update(order);
				
				return "success";
			} else {
				System.out.println("notify_url 验证失败");
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}
}
