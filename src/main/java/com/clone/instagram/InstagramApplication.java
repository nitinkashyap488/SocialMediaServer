package com.clone.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstagramApplication {

	public static void main(String[] args) {
		String[] jvmArgs = { "-Xss2m" };
		SpringApplication.run(InstagramApplication.class, jvmArgs);
	}

}
