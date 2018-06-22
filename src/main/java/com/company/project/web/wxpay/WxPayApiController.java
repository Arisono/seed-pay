package com.company.project.web.wxpay;

import com.jpay.weixin.api.WxPayApiConfig;

public abstract class WxPayApiController{
	//App配置
	public abstract WxPayApiConfig getApiConfig();
	//小程序配置
	public abstract WxPayApiConfig getWxApiConfig();
}