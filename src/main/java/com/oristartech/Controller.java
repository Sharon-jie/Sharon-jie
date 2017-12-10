package com.oristartech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.dubbo.config.annotation.Service;

@SpringBootApplication
public class Controller {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigurableApplicationContext cxt = SpringApplication.run(Controller.class, args);
//		String[] beanNames = cxt.getBeanDefinitionNames();
		String[] beanNames = cxt.getBeanNamesForAnnotation(Service.class);
		System.out.println("所以beanNames个数："+beanNames.length);
		for (String string : beanNames) {
			System.out.println(string);
		}
		
	}

}
