package com.clone.instagram.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.User;
import com.clone.instagram.repository.UserRepository;
import com.clone.instagram.service.UserService;

@RestController
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping("/signin")
	public ResponseEntity<User> signinHandler(Authentication authentication) {
		User user = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new BadCredentialsException("Invalid Username or password"));
		return ResponseEntity.ok(user);
	}

	@PostMapping("/signup")
	public ResponseEntity<User> registerUserHandler(@RequestBody User newUser) throws UserException {
		User createdUser;
		try {
			createdUser = userService.registerUser(newUser);
			log.info("User registered successfully: {}", createdUser);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
		} catch (UserException ex) {
			throw new UserException("User registration failed: " + ex.getMessage());
		}
	}
}
