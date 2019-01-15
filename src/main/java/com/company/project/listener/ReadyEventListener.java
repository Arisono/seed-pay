package com.company.project.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ReadyEventListener  implements ApplicationListener<ApplicationReadyEvent>{
    @SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("||:应用启动完成-----------------");
	}
  
}
