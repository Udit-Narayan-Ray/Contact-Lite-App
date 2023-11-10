package com.contact;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartContactApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartContactApplication.class, args);
		System.out.println("JSRK");
		
		System.out.println();
	}

	@Bean
	ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
}
