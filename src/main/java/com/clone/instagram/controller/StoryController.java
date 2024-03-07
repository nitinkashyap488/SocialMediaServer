package com.clone.instagram.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clone.instagram.exception.StoryException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Story;
import com.clone.instagram.modal.User;
import com.clone.instagram.service.StoryService;
import com.clone.instagram.service.UserService;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

	private static final Logger log = LoggerFactory.getLogger(StoryController.class);

	@Autowired
	private StoryService storyService;

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<Story> createStory(@RequestBody Story newStory,
			@RequestHeader("Authorization") String authToken) throws UserException {
		User requestingUser = userService.findUserProfile(authToken);
		Story createdStory = storyService.createStory(newStory, requestingUser.getId());
		log.info("New story created by user {}", requestingUser.getId());
		return ResponseEntity.ok(createdStory);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<Story>> findAllStoryByUserId(@PathVariable Integer userId)
			throws UserException, StoryException {
		List<Story> userStories = storyService.findStoryByUserId(userId);
		log.info("Found {} stories for user {}", userStories.size(), userId);
		return ResponseEntity.ok(userStories);
	}
}
