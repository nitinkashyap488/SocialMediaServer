package com.clone.instagram.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.User;
import com.clone.instagram.response.MessageResponse;
import com.clone.instagram.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Integer id) throws UserException {
		User user = userService.findUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) throws UserException {
		User user = userService.findUserByUsername(username);
		return ResponseEntity.ok(user);
	}

	@PutMapping("/follow/{followUserId}")
	public ResponseEntity<MessageResponse> followUser(@RequestHeader("Authorization") String token,
			@PathVariable Integer followUserId) throws UserException {
		User requestingUser = userService.findUserProfile(token);
		String message = userService.followUser(requestingUser.getId(), followUserId);
		return ResponseEntity.ok(new MessageResponse(message));
	}

	@PutMapping("/unfollow/{unfollowUserId}")
	public ResponseEntity<MessageResponse> unfollowUser(@RequestHeader("Authorization") String token,
			@PathVariable Integer unfollowUserId) throws UserException {
		User requestingUser = userService.findUserProfile(token);
		String message = userService.unfollowUser(requestingUser.getId(), unfollowUserId);
		return ResponseEntity.ok(new MessageResponse(message));
	}

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) throws UserException {
		User user = userService.findUserProfile(token);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/multi/{userIds}")
	public ResponseEntity<List<User>> getUsersByUserIds(@PathVariable List<Integer> userIds) {
		List<User> users = userService.findUsersByUserIds(userIds);
		log.info("UserIds: {}", userIds);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUser(@RequestParam("username") String query) throws UserException {
		List<User> users = userService.searchUser(query);
		return ResponseEntity.ok(users);
	}

	@PutMapping("/account/edit")
	public ResponseEntity<User> updateUserDetails(@RequestHeader("Authorization") String token, @RequestBody User user)
			throws UserException {
		User requestingUser = userService.findUserProfile(token);
		User updatedUser = userService.updateUserDetails(user, requestingUser);
		return ResponseEntity.ok(updatedUser);
	}
}
