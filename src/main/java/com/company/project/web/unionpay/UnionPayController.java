package com.company.project.web.unionpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jpay.ext.kit.DateKit;
import com.jpay.ext.kit.HttpKit;
import com.jpay.unionpay.AcpService;
import com.jpay.unionpay.LogUtil;
import com.jpay.unionpay.SDKConfig;
import com.jpay.unionpay.UnionPayApi;
import com.jpay.unionpay.UnionPayApiConfig;
import com.jpay.vo.AjaxResult;



/**
 * 银联支付
 * 目前jpay银联支付模块用的网络连接用到的是Okhttp，所以需要引入Okhttp相关架包
 * @author Arison
 * 
 */
@Controller
@RequestMapping("/unionpay")
public class UnionPayController {
	private static final Logger logger = LoggerFactory.getLogger(UnionPayController.class);
	private AjaxResult ajax = new AjaxResult();
	
	@RequestMapping("")
	@ResponseBody
	public String index() {
		logger.info("欢迎使用银联支付接口");
		return "欢迎使用银联支付接口";
	}
	
	/**
	 * APP支付获取tn
	 */
	@RequestMapping(value = "/appConsume",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public AjaxResult appConsume() {
		try {
			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setChannelType("08")//渠道类型	channelType  08是移动
					.setMerId("777290058159097")//商户代码	merId 777290058159097
					.setTxnAmt("1")//单位为分，不能带小数点，样例：1元送100
					.setReqReserved("reqReserved")//请求方自定义域
					.setBackUrl(SDKConfig.getConfig().getBackUrl())//后台通知地址	
					.createMap();
			/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
			Map<String, String> rspData = UnionPayApi.AppConsumeByMap(reqData);
			// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
			if (!rspData.isEmpty()) {
				if (AcpService.validate(rspData, "UTF-8")) {
					logger.info("验证签名成功");
					String respCode = rspData.get("respCode");
					if (("00").equals(respCode)) {
						// 成功,获取tn号
						String tn = rspData.get("tn");
						ajax.success(tn);
					} else {
						// 其他应答码为失败请排查原因或做失败处理
						ajax.addError(respCode);
					}
				} else {
					logger.error("验证签名失败");
					// 检查验证签名失败的原因
					ajax.addError("验证签名失败");
				}
			} else {
				// 未返回正确的http状态
				logger.error("未获取到返回报文或返回http状态码非200");
				ajax.addError("未获取到返回报文或返回http状态码非200");
			}
			String reqMessage = getHtmlResult(reqData);
			String rspMessage = getHtmlResult(rspData);
			logger.info("app>reqMessage>>>" + reqMessage + " rspMessage>>>" + rspMessage);
			return ajax;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 组装请求，返回报文字符串用于显示
	 * 
	 * @param data
	 * @return {String}
	 */
	public static String getHtmlResult(Map<String, String> data) {

		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			String key = en.getKey();
			String value = en.getValue();
			if ("respCode".equals(key)) {
				sf.append("<b>" + key + "=" + value + "</br></b>");
			} else
				sf.append(key + "=" + value + "</br>");
		}
		return sf.toString();
	}
	
	
	/**
	 * 订单状态查询
	 * B2C跟B2B查询区别就在于bizType的不同
	 */
	@RequestMapping(value = "/query",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void query(HttpServletResponse response,
			@RequestParam("orderId") String orderId,
			@RequestParam("txnTime") String txnTime){
		try {
			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("00")
					.setTxnSubType("00")
					.setBizType("000301")
					.setMerId("777290058159097")
					.setOrderId(orderId)
					.setTxnTime(txnTime)
					.createMap();
			Map<String, String> rspData = UnionPayApi.singleQueryByMap(reqData);
			if(!rspData.isEmpty()){
				if(AcpService.validate(rspData, "UTF-8")){
					logger.info("验证签名成功");
					if("00".equals(rspData.get("respCode"))){//如果查询交易成功
						//处理被查询交易的应答码逻辑
						String origRespCode = rspData.get("origRespCode");
						if("00".equals(origRespCode)){
							//交易成功，更新商户订单状态
						}else if("03".equals(origRespCode) ||
								 "04".equals(origRespCode) ||
								 "05".equals(origRespCode)){
							//需再次发起交易状态查询交易 
						}else{
							//其他应答码为失败请排查原因
						}
					}else{//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
					}
				}else{
					logger.error("验证签名失败");
					// 检查验证签名失败的原因
				}
			}else{
				//未返回正确的http状态
				logger.error("未获取到返回报文或返回http状态码非200");
			}
			String reqMessage = getHtmlResult(reqData);
			String rspMessage = getHtmlResult(rspData);
			response.getWriter().println("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 退款接口
	 * @param response
	 * @param origOryId  ：原始交易订单号
	 * @param free ：退款金额
	 */
	@RequestMapping(value = "/refund",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void refund(HttpServletResponse response,
			@RequestParam("origOryId") String origOryId,
			@RequestParam("free")  String free){
		try {
			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("04") //交易类型 04-退货	31-消费撤销
					.setTxnSubType("00") //交易子类型  默认00
					.setBizType("000201") //业务类型 B2C网关支付，手机wap支付
					.setChannelType("08") //渠道类型，07-PC，08-手机
					.setMerId("777290058159097")//商户号
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效	
					.setTxnAmt(free)
					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.setOrigQryId(origOryId) //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
					.createMap();
			Map<String, String> rspData = UnionPayApi.backRequestByMap(reqData);
			if(!rspData.isEmpty()){
				if(AcpService.validate(rspData, "UTF-8")){
					logger.info("验证签名成功");
					String respCode = rspData.get("respCode");
					if("00".equals(respCode)){
						//交易已受理，等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
					}else if("03".equals(respCode)|| 
							 "04".equals(respCode)||
							 "05".equals(respCode)){
						//后续需发起交易状态查询交易确定交易状态
					}else{
						//其他应答码为失败请排查原因
					}
				}else{
					logger.error("验证签名失败");
					// 检查验证签名失败的原因
				}
			}else{
				//未返回正确的http状态
				logger.error("未获取到返回报文或返回http状态码非200");
			}
			
			String reqMessage = getHtmlResult(reqData);
			String rspMessage = getHtmlResult(rspData);
			response.getWriter().println("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 后台回调
	 */
	@RequestMapping(value = "/backRcvResponse",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String backRcvResponse(HttpServletRequest request) {
		logger.info("BackRcvResponse接收后台通知开始");

		String encoding = "UTF-8";

		//String notifyStr = HttpKit.readData(request);
		//System.out.println("返回参数 str："+notifyStr);
		// 获取银联通知服务器发送的后台通知参数
		//Map<String, String> reqParam = getAllRequestParamToMap(notifyStr);
		Map<String, String> reqParam = getAllRequestParam(request);
		System.out.println("返回参数 map："+JSON.toJSONString(reqParam));

		// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(reqParam, encoding)) {
			logger.info("后台验证签名结果[失败].");
			// 验签失败，需解决验签问题

		} else {
			logger.info("后台验证签名结果[成功].");
			// 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态

			String orderId = reqParam.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
			String respCode = reqParam.get("respCode");
			// 判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
			logger.info("orderId>>>" + orderId + " respCode>>" + respCode);
		}
		logger.info("BackRcvResponse接收后台通知结束");
		// 返回给银联服务器http 200 状态码
		return "ok";
	}

	/**
	 * 前台回调
	 */
	@RequestMapping(value = "/frontRcvResponse",method={RequestMethod.POST,RequestMethod.GET})
	public String frontRcvResponse(HttpServletRequest request,HttpServletResponse response) {
		try {
			logger.info("FrontRcvResponse前台接收报文返回开始");

			String encoding = "UTF-8";
			logger.info("返回报文中encoding=[" + encoding + "]");
			String readData = HttpKit.readData(request);
			Map<String, String> respParam = getAllRequestParamToMap(readData);

			// 打印请求报文
			LogUtil.printRequestLog(respParam);

			Map<String, String> valideData = null;
			StringBuffer page = new StringBuffer();
			if (null != respParam && !respParam.isEmpty()) {
				Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
				valideData = new HashMap<String, String>(respParam.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					value = new String(value.getBytes(encoding), encoding);
					page.append("<tr><td width=\"30%\" align=\"right\">" + key + "(" + key + ")</td><td>" + value
							+ "</td></tr>");
					valideData.put(key, value);
				}
			}
			if (!AcpService.validate(valideData, encoding)) {
				page.append("<tr><td width=\"30%\" align=\"right\">前台验证签名结果</td><td>失败</td></tr>");
				logger.info("前台验证签名结果[失败].");
			} else {
				page.append("<tr><td width=\"30%\" align=\"right\">前台验证签名结果</td><td>成功</td></tr>");
				logger.info("前台验证签名结果[成功].");
				String orderId = valideData.get("orderId"); // 其他字段也可用类似方式获取

				String respCode = valideData.get("respCode");
				// 判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
				logger.info("orderId>>>" + orderId + " respCode>>" + respCode);
			}
			
			request.setAttribute("result", page.toString());
			logger.info("FrontRcvResponse前台接收报文返回结束");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "utf8_result.html";
	}

	
	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
	
	/**
	 * 将回调参数转为Map
	 * 
	 * @param notifyStr
	 * @return {Map<String, String>}
	 */
	public static Map<String, String> getAllRequestParamToMap(final String notifyStr) {
		Map<String, String> res = new HashMap<String, String>();
		try {
			logger.info("收到通知报文：" + notifyStr);
			String[] kvs = notifyStr.split("&");
			for (String kv : kvs) {
				String[] tmp = kv.split("=");
				if (tmp.length >= 2) {
					String key = tmp[0];
					String value = URLDecoder.decode(tmp[1], "UTF-8");
					res.put(key, value);
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":"
					+ e.getMessage());
		}
		return res;
	}

}
