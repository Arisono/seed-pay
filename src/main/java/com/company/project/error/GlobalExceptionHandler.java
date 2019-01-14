package com.company.project.error;

import com.alibaba.fastjson.JSON;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


//@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("exceptionStr", JSON.toJSONString(e));
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}

