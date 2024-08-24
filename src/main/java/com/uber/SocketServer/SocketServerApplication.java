package com.uber.SocketServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketServerApplication.class, args);
	}

}
