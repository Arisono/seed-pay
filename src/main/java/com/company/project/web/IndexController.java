package com.company.project.web;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("unused")
@RestController
public class IndexController {

	 @GetMapping("/errorInfo")
	 public String getErrorInfo(@RequestParam( value="message") String msg){
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		 System.out.println("msg:"+msg+" time:"+ df.format(new Date()));
		 return msg;
	 }

	@GetMapping("/logs")
	public String getLogsInfo(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time=df.format(new Date());
		System.out.println(" time:"+ time);
		return time;
	}
}
