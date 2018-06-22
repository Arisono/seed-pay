package com.company.project.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
@RestController
public class IndexController {


	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView layout(ModelMap map) {
		map.addAttribute("host", "seed-pay");
		return new ModelAndView("/index");
	}
}
