package com.company.project.web.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;

@RestController 
@RequestMapping("/mail")  
public class EmailController {
	@Autowired  
    JavaMailSender jms;
	
	 @GetMapping("/errorInfo")
	 public String getErrorInfo(@RequestParam( value="message") String msg){
		    System.out.println("msg:"+msg);
			SimpleMailMessage mainMessage = new SimpleMailMessage();
			//发送者  
			mainMessage.setFrom("aliyun_services@sina.com");
			//接收者  
			mainMessage.setTo("784602719@qq.com");
			//发送的标题  
			mainMessage.setSubject("APPBUG日志");
			//发送的内容  
			mainMessage.setText(msg);
			jms.send(mainMessage);
		 return msg;
	 }
	
    /**
     * 邮件单发
     * @return
     */
    @GetMapping("/send")
	public Result send(){
    	//建立邮件消息  
		SimpleMailMessage mainMessage = new SimpleMailMessage();
		//发送者  
		mainMessage.setFrom("aliyun_services@sina.com");
		//接收者  
		mainMessage.setTo("784602719@qq.com");
		//发送的标题  
		mainMessage.setSubject("欢迎订阅专业的技术网站");
		//发送的内容  
		mainMessage.setText("请注意查收：欢迎访问网站  https://www.yundashi168.com/");
		jms.send(mainMessage);

		return  ResultGenerator.genSuccessResult(mainMessage);
    }
    
    
    /**
     * 邮件群发
     * @return
     */
    @GetMapping("/sendAll")
    public Result sendAll(){
    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();//直接生产一个实例
    	String users[] = {"784602719@qq.com","869554745@qq.com","arisono@aliyun.com"};
    	mailSender.setHost("smtp.sina.com");
    	mailSender.setPassword("jie.13237658359");
    	mailSender.setPort(25);
    	mailSender.setProtocol("smtp");
    	mailSender.setUsername("aliyun_services@sina.com");
    	SimpleMailMessage message = new SimpleMailMessage();
    	message.setFrom("aliyun_services@sina.com");
    	message.setTo(users); // 群发
    	message.setSubject("腾讯云服务器爆品抢购进行中1核1G1年仅168元");
    	message.setText("http://cloud.yundashi168.com/archives/217"
    			+ " 腾讯云服务器爆品抢购进行中1核1G1年仅168元！还有四天！");
    	mailSender.send(message);
    	return  ResultGenerator.genSuccessResult(message);
    }
}
