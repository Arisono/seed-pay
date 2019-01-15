package com.company.project.web.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;

@RestController 
@RequestMapping("/mail")  
public class EmailController {
	@Autowired  
    JavaMailSender jms;
	
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
    	message.setSubject("阿里云学生组团购买服务器送域名活动火热进行中");
    	message.setText("欢迎访问：https://www.yundashi168.com/articles/2019/01/08/1546941171651.html"
    			+ "  这里有最新，最全的技术资料分享。请记得收藏和关注我们哦！");
    	mailSender.send(message);
    	return  ResultGenerator.genSuccessResult(message);
    }
}
