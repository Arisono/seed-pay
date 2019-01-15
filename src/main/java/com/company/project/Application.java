package com.company.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.company.project.config.StartupRunner;
import com.company.project.listener.StartListener;


@SpringBootApplication
public class Application  extends SpringBootServletInitializer{
	
    public static void main(String[] args) {   	
        SpringApplication sa = new SpringApplication(Application.class);
        sa.addListeners(new StartListener());//监听应用启动  其它监听事件在初始化完成之后自动监听
        sa.run(args);          
    }
      
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {		
		return application.sources(Application.class);
	}
    
	 @Bean
     public StartupRunner startupRunner(){
         return new StartupRunner();
     }
	 
}