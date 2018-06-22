package com.company.project.web.wxpay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.model.Order;
import com.company.project.model.WxPayBean;
import com.company.project.service.OrderService;
import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.IpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.ext.kit.StrKit;
import com.jpay.util.StringUtils;
import com.jpay.vo.AjaxResult;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApiConfig;
import com.jpay.weixin.api.WxPayApiConfigKit;
import com.jpay.weixin.api.WxPayApiConfig.PayModel;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;


@Controller
@RequestMapping("/wxpay")
public class WxPayController extends WxPayApiController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private AjaxResult result = new AjaxResult();
	@Resource
	private OrderService orderService;
	@Autowired
	WxPayBean wxPayBean;
	String notify_url;
	
	@Override
	public WxPayApiConfig getApiConfig() {
		notify_url = wxPayBean.getDomain().concat("/wxpay/pay_result");
		return WxPayApiConfig.New()
				.setAppId(wxPayBean.getAppId())
				.setMchId(wxPayBean.getMchId())
				.setPaternerKey(wxPayBean.getPartnerKey())
				.setPayModel(PayModel.BUSINESSMODEL);
	}

	@Override
	public WxPayApiConfig getWxApiConfig() {
		return WxPayApiConfig.New()
				.setAppId("wx172657dad29220cc")
				.setMchId(wxPayBean.getMchId())
				.setPaternerKey(wxPayBean.getPartnerKey())
				.setPayModel(PayModel.BUSINESSMODEL);
	}
	
	
	/**
	 * 查询订单列表
	 * @param userId
	 * @param tradeState
	 * @return
	 */
	@RequestMapping(value = "/wxAppQuery")
	@ResponseBody
	public JSONArray orderquery(@RequestParam("userid") String userId,
			@RequestParam(value="tradeState",required=false,defaultValue = "") String tradeState){
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
			
			c3.andCondition("payType=", "微信");
			condition.and(c3);
		
		    List<Order> orders= orderService.findByCondition(condition);
		    JSONArray json = new JSONArray();
			for(Order order : orders){
				JSONObject jo = new JSONObject();
				jo.put("outTradeNo", order.getOrderid());
				jo.put("transactionId",order.getTransactionid());
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
	
	
	/**
	 * app支付
	 */
	@RequestMapping(value = "/appPay")
	@ResponseBody
	public AjaxResult appPay(HttpServletRequest request,
			@RequestParam("userid")String userId,
			@RequestParam("fee") Float fee,
			@RequestParam(value="out_trade_no",required=false)String outTradeNo){
		log.info("---------------------微信支付下单------------------------------");
		//不用设置授权目录域名
		//统一下单地址 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1#
		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		String orderid;
		if(StringUtils.isEmpty(outTradeNo)){
			orderid=StringUtils.getOutTradeNo();
		}else{
			orderid=outTradeNo;
		}
		Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
				.setAttach("微信支付测试  -By Arison")
				.setBody("微信 App付测试  -By Arison")
				.setSpbillCreateIp(ip)
				.setTotalFee(String.valueOf((int)(fee*100)))
				.setOutTradeNo(orderid)
				.setTradeType(WxPayApi.TradeType.APP)
				.setNotifyUrl(notify_url)
				
				.build();
		log.info("params:"+JSON.toJSONString(params));
		//支付
		String xmlResult =  WxPayApi.pushOrder(false,params);
		
		Map<String, String> resultMap = PaymentKit.xmlToMap(xmlResult);
		
		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.info(xmlResult);
			result.addError(return_msg);
			return result;
		}
		String result_code = resultMap.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.info(xmlResult);
			result.addError(return_msg);
			return result;
		}
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回

		String prepay_id = resultMap.get("prepay_id");
		//封装调起微信支付的参数 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12

		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", WxPayApiConfigKit.getWxPayApiConfig().getAppId());
		packageParams.put("partnerid", WxPayApiConfigKit.getWxPayApiConfig().getMchId());
		packageParams.put("prepayid", prepay_id);
		packageParams.put("package", "Sign=WXPay");//Sign=WXPay
		packageParams.put("noncestr", System.currentTimeMillis() + "");
		packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
		String packageSign = PaymentKit.createSign(packageParams, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
		packageParams.put("sign", packageSign);
		
		String jsonStr = JSON.toJSONString(packageParams);
        log.info("最新返回apk的参数:"+jsonStr);
		result.success(jsonStr);
		
		Order order=new Order();
		System.out.println("商户订单号："+outTradeNo);
		order.setOrderid(orderid);
		order.setPaytype("微信");
		order.setBody("微信支付测试  -By Arison");
		order.setUserid(userId);
		order.setTimeCreate(new Date());
		order.setFee(fee);
		order.setTradestate(0);
		if (StringUtils.isEmpty(outTradeNo)) {
			orderService.save(order);
		}else{
			orderService.update(order);
		}
		return result;
	}
	
	
	/**
	 * 支付回调
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pay_result",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String pay_notify(HttpServletRequest request) {
		 // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
         log.info("--------------微信支付回调！--------------------------");
		 String xmlMsg = HttpKit.readData(request);
		 Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		 System.out.println("微信服务器--->服务器：json="+JSON.toJSONString(params));
		 String result_code=params.get("result_code");
		 String out_trade_no=params.get("out_trade_no");
		 String transaction_id=params.get("transaction_id");
		if ("SUCCESS".equals(result_code)) {
			System.out.println("支付成功！"+"out_trade_no:"+out_trade_no
					+"transaction_id:"+transaction_id);
			Order order=new Order();
			order.setOrderid(out_trade_no);
			order.setTransactionid(transaction_id);
			order.setTradestate(1);
			order.setTimePayment(new Date());
			orderService.update(order);
			Map<String, String> xml = new HashMap<String, String>();
			xml.put("return_code", "SUCCESS");
			xml.put("return_msg", "OK");
			return PaymentKit.toXml(xml);
		}else{
			
		}
		return xmlMsg;
	}

	
	/**
	 * 退款 需要双向证书！
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appRefund",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject orderRefund(@RequestParam("fee")Float fee, 
			@RequestParam("refund_fee")Float refund_fee, 
			@RequestParam(value="transaction_id",required=false)String transaction_id, 
			@RequestParam(value="outTradeNo",required=false)String outTradeNo, 
			HttpServletRequest request){
		String certPath=request.getServletContext().getRealPath("/")+"WEB-INF"+"\\apiclient_cert.p12";
		String certPass=WxPayApiConfigKit.getWxPayApiConfig().getMchId();
		System.out.println("证书路径："+certPath);
		System.out.println("证书密码："+certPass);
		WxPayApiConfigKit.getWxPayApiConfig().setContractNotifyUrl(wxPayBean.getDomain().concat("/wxpay/refund_result"));
		//调用证书
		Map<String, String> params =new HashMap<>();	
		params.put("appid", WxPayApiConfigKit.getWxPayApiConfig().getAppId());
		params.put("mch_id",WxPayApiConfigKit.getWxPayApiConfig().getMchId());
		params.put("nonce_str",System.currentTimeMillis() + "");//随机字符串-随机数
		String s = System.currentTimeMillis() + "";
		params.put("out_refund_no",s);//商户退款单号 -随机数
		params.put("out_trade_no", outTradeNo);//商户订单号  二选一
		params.put("transaction_id",transaction_id);//微信订单号 二选一
		System.out.println(refund_fee);
		System.out.println(fee);
		System.out.println(transaction_id);
		System.out.println(outTradeNo);
		params.put("refund_fee",String.valueOf((int)(refund_fee*100)));
		params.put("total_fee", String.valueOf((int)(fee*100)));
		params.put("notify_url", WxPayApiConfigKit.getWxPayApiConfig().getContractNotifyUrl());
		params.put("sign", PaymentKit.createSign(params, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey()));
		
	
		System.out.println("微信退款参数："+JSON.toJSONString(params));

		Order order=new Order();
		order.setOrderid(outTradeNo);
		order.setTransactionid(transaction_id);
		order.setRefundfee(Double.valueOf(refund_fee));
		order.setTradestate(2);
		order.setOrderrefundid(s);;
		order.setTimeRefund(new Date());
		orderService.update(order);
		
	    String info= WxPayApi.orderRefund(false, params, certPath, certPass);
	    System.out.println("微信退款：xml"+info);
	    System.out.println("微信退款：json"+JSON.toJSONString(PaymentKit.xmlToMap(info)));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success",true);
		jsonObject.put("data",info);
		
		return jsonObject;
	}
	
	
	/**
	 * 退款回调
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/refund_result",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String refund_notify(HttpServletRequest request) {
		System.out.println("----------------微信退款回调-----------------------");
		String xmlMsg = HttpKit.readData(request);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		System.out.println("微信服务器--->服务器 参数：="+JSON.toJSONString(params));
		String return_code  = params.get("return_code");
		if("SUCCESS".equals(return_code)){
		
		}
		return notify_url;
	}
}
