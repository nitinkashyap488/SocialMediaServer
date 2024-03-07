package com.clone.instagram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String homeControllerHandler() {
		return "Welcome to Instagram backend API";
	}
}
