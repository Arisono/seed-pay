package com.company.project.web.push;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.core.ResultGenerator;
import com.github.kevinsawicki.http.HttpRequest;



/**
 * 微信推送业务处理
 * @author Arison
 *
 */
@RestController
public class WeiXinPushController {
	
	private static final String APPID="wxbc1f8607137d3b8a";
	
	private static final String  AppSecret ="cadf13c4e21c2c122cb2341b341e5c22";
	
	
	/**
	 * 绑定手机号码与微信公众号OpenId
	 * @param request
	 * @return
	 */
	@RequestMapping("/wxlogin")
	public Result wxlogin(HttpServletRequest request) {
		String code=request.getParameter("code");
		String phone =request.getParameter("state");
		
		HashMap<String, Object> params=new HashMap<>();
		params.put("appid", APPID);
		params.put("secret", AppSecret);
		params.put("code", code);
		params.put("grant_type", "authorization_code");
		HttpRequest response= HttpRequest.get("https://api.weixin.qq.com/sns/oauth2/access_token", params, true);
        String result=response.body();
		String openid=JSON.parseObject(result).getString("openid");
		
		HttpRequest hRequest=HttpRequest.get("https://mobile.ubtob.com/user/appWecharId")
				 .form("telephone", phone)
				 .form("openid", openid);
				String isBind= hRequest.body();
		if (JSON.parseObject(isBind).getString("result").equals("true")) {
			return ResultGenerator.genSuccessResult("绑定成功！").setData(isBind);
		}else{
			return ResultGenerator.genFailResult("绑定失败！");
		}
	}
	
	@RequestMapping("/wxBind")
	public Result wxBind(HttpServletRequest request){
		String phone =request.getParameter("phone");
		String openid =request.getParameter("openid");
		if (StringUtils.isEmpty(phone)) {
			return ResultGenerator.genFailResult("缺少参数：phone");
		}
		if (StringUtils.isEmpty(openid)) {
			return ResultGenerator.genFailResult("缺少参数：openid");
		}
	    HttpRequest hRequest=HttpRequest.get("https://mobile.ubtob.com/user/appWecharId")
		 .form("telephone", phone)
		 .form("openid", openid);
		String isBind= hRequest.body();
		if (JSON.parseObject(isBind).getString("result").equals("true")) {
			return ResultGenerator.genSuccessResult("绑定成功！").setData(isBind);
		}else{
			return ResultGenerator.genFailResult("绑定失败！");
		}
	}
  
	@RequestMapping("/wxPush")
	public Result wxPush(HttpServletRequest request){
	    String phone=request.getParameter("phone");//必填
		if (StringUtils.isEmpty(phone)) {
			return ResultGenerator.genFailResult("缺少参数：phone")
					.setData("请查阅接口文档！")
					.setCode(ResultCode.INTERNAL_SERVER_ERROR);
		}	
		 HttpRequest eRequest=  HttpRequest.get("https://mobile.ubtob.com/user/appGetOpenid").form("telephone", phone);
		 String hresult= eRequest.body();
		 String openid=JSON.parseObject(hresult).getString("openid");
		 if ("0".equals(openid)) {
			return ResultGenerator.genFailResult("该用户未进行微信公众登录绑定").setData(hresult);
		}

		String fieldMap=request.getParameter("fieldMap");
		String url=request.getParameter("url");
		if (StringUtils.isEmpty(fieldMap)) {
			return ResultGenerator.genFailResult("缺少参数：fieldMap");
		}
		if (StringUtils.isEmpty(url)) {
			return ResultGenerator.genFailResult("缺少参数：url");
		}
		
//		String 	url="http://www.aliyunyh.com/480.html";
//		String fieldMap="{\"title\":\"UAS流程审批提醒\",\"time\":\"2018年10月16日星期二 14:56\",\"content\":\"陈虎的员工转正申请单等待您的审批！\"}";
		
		String title=JSON.parseObject(fieldMap).getString("title");
		String time=JSON.parseObject(fieldMap).getString("time");
		String content=JSON.parseObject(fieldMap).getString("content");
		String json="{\"touser\":\""+openid+"\","
				+ "\"template_id\":\"siCbcBD_czFdqQpgs0q-PTboFg-SjaUpDadPEqzdpJc\","
				+ "\"url\":\""+""+url+""+"\","
				+ "\"data\":{"
				+ "\"first\":{\"value\":\""+title+"\","
				+"\"color\":\"#173177\"},"
				+ "\"keyword1\":{\"value\":\""+content+"\",\"color\":\"#173177\"},"
				+ "\"keyword2\":{\"value\":\""+time+"\",\"color\":\"#173177\"},"
				+ "\"remark\":{\"value\":\"点击查看详情！！！\",\"color\":\"#173177\"}}}";
		String access_token=  getWxAcessToken();		
        String token=JSON.parseObject(access_token).getString("access_token");
        HttpRequest hRequest=  HttpRequest.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token)
        		.header("Content-Type", "application/json")
        		.send(json.getBytes());
        String result= hRequest.body();
   
        if(JSON.parseObject(result).getInteger("errcode")==0&&JSON.parseObject(result).getString("errmsg").equals("ok")){
        	return ResultGenerator.genSuccessResult("推送成功！").setData(result);
        }else{
        	return ResultGenerator.genFailResult("推送失败！").setData(result);        	
        }
	}
	
	
	public String getWxAcessToken(){
		HashMap<String, Object> params=new HashMap<>();
		params.put("appid", APPID);
		params.put("secret", AppSecret);
		params.put("grant_type", "client_credential");
		HttpRequest httpRequest=HttpRequest.get("https://api.weixin.qq.com/cgi-bin/token",params,false);
		String content=httpRequest.body();
		return content;
	}
	
	public static void testSendBody(){
		String json="{\"touser\":\""+"o8lZ9uGnn074M2wiP_5cWsZ3NL8s"+"\","
				+ "\"template_id\":\"oi4SokVV7Is0kZz5w8VJG1b3zrLWtApqftCN4iJ3Iyc\","
				+ "\"url\":\""+"http://www.aliyunyh.com/480.html"+"\","
				+ "\"data\":{"
				+ "\"first\":{\"value\":\"刘杰向您提交了请假条！\","
				+"\"color\":\"#173177\"},"
				+ "\"keyword1\":{\"value\":\"骑车去旅行\",\"color\":\"#173177\"},"
				+ "\"keyword2\":{\"value\":\"事假\",\"color\":\"#173177\"},"
				+ "\"keyword3\":{\"value\":\"2018年09月30日 12:00到18:00\",\"color\":\"#173177\"},"
				+ "\"keyword4\":{\"value\":\"半天\",\"color\":\"#173177\"},"
				+ "\"remark\":{\"value\":\"点击模板URL进入调转界面！！！\",\"color\":\"#173177\"}}}";
		    //http://192.168.253.200/postBodyByString
		    HttpRequest httpRequest = null;
			httpRequest = HttpRequest.post("http://192.168.253.200/postBodyByString")
					.header("Content-Type", "application/json")
					.send(json.getBytes());
		System.out.println(httpRequest.body());
	}
	
	public static void main(String[] args) {
		 HttpRequest jRequest=  HttpRequest.get("https://mobile.ubtob.com/user/appGetOpenid").form("telephone", "13266699268");
		 String jresult= jRequest.body();
		 System.out.println(""+jresult);
		 
		 HttpRequest hRequest=HttpRequest.get("https://mobile.ubtob.com/user/appWecharId")
				 .form("telephone", "13266699268")
				 .form("openid", "o8lZ9uGnn074M2wiP_5cWsZ3NL8s");
		 System.out.println(hRequest.body());
	}
	
}
