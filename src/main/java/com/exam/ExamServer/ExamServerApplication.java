package com.exam.ExamServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExamServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamServerApplication.class, args);
	}

}
