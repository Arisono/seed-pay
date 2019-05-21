package com.company.project.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("unused")
@RestController
public class IndexController {

	 @GetMapping("/errorInfo")
	 public String getErrorInfo(@RequestParam( value="message") String msg){
		 System.out.println("msg:"+msg);
		 return msg;
	 }
}
