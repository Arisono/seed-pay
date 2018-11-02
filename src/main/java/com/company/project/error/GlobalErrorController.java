package com.company.project.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 主要用途：统一处理错误/异常(针对控制层)
 *
 * @author Arison
 */
@Controller
@RequestMapping("${server.error.path:/error}")
public class GlobalErrorController implements ErrorController  {
	@Autowired
	private ErrorInfoBuilder errorInfoBuilder;// 错误信息的构建工具.
	private final static String DEFAULT_ERROR_VIEW = "error2";// 错误信息页
	
	@Override
	public String getErrorPath() {
		System.out.println("-------------getErrorPath()--------------------");
		return errorInfoBuilder.getErrorProperties().getPath();
	}

	/**
	 * 情况1：若预期返回类型为text/html,则返回错误信息页(View).
	 */
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request) {
		System.out.println("-------------普通请求--------------------");
		System.out.println("状态码：" + errorInfoBuilder.getHttpStatus(request));
//		if(errorInfoBuilder.getHttpStatus(request).value()==404){
//			return new ModelAndView("index", "errorInfo",
//					errorInfoBuilder.getErrorInfo(request));
//		}
		
		return new ModelAndView(DEFAULT_ERROR_VIEW, "errorInfo",
				errorInfoBuilder.getErrorInfo(request));
	}

	/**
	 * 情况2：其它预期类型 则返回详细的错误信息(JSON).
	 */
	@RequestMapping
	@ResponseBody
	public ErrorInfo error(HttpServletRequest request) {
		System.out.println("-------------Axjax请求--------------------");
		return errorInfoBuilder.getErrorInfo(request);
	}

}
