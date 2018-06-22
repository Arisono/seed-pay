package com.company.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.company.project.web.wxpay.WxPayApiController;
import com.company.project.web.wxpay.WxPayController;
import com.jpay.weixin.api.WxPayApiConfigKit;

public class WxPayInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object handler) throws Exception {
		if (HandlerMethod.class.equals(handler.getClass())) {
			HandlerMethod method = (HandlerMethod) handler;
			Object controller = method.getBean();
			if (controller instanceof WxPayApiController == false) {
				throw new RuntimeException("控制器需要继承 WxPayApiController");
			}
			System.out.println("config appID:"+((WxPayController)controller).getApiConfig().getAppId());
			System.out.println("config mchID:"+((WxPayController)controller).getApiConfig().getMchId());
			try {
				//微信配置文件
				WxPayApiConfigKit.setThreadLocalWxPayApiConfig(((WxPayController)controller).getApiConfig());
				//小程序配置文件
				WxPayApiConfigKit.setThreadLocalWxPayApiConfig(((WxPayController)controller).getWxApiConfig());
				return true;
			}
			finally {
			}
		}
		return false;
	}

}
