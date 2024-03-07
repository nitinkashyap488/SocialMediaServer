package com.clone.instagram.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clone.instagram.dto.UserDto;
import com.clone.instagram.exception.StoryException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Story;
import com.clone.instagram.modal.User;
import com.clone.instagram.repository.StoryRepository;
import com.clone.instagram.repository.UserRepository;

@Service
public class StoryServiceImplementation implements StoryService {

	private static final Logger log = LoggerFactory.getLogger(StoryServiceImplementation.class);

	@Autowired
	private StoryRepository storyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Story createStory(Story story, Integer userId) throws UserException {
		User user = userService.findUserById(userId);
		UserDto userDto = createUserDto(user);
		story.setUserDto(userDto);
		story.setTimestamp(LocalDateTime.now());
		user.getStories().add(story);
		Story createdStory = storyRepository.save(story);
		log.info("New story created by user {}", user.getId());
		return createdStory;
	}

	private UserDto createUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setUserImage(user.getImage());
		return userDto;
	}

	@Override
	public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException {
		User user = userService.findUserById(userId);
		List<Story> userStories = user.getStories();
		if (userStories.isEmpty()) {
			throw new StoryException("This user doesn't have any story.");
		}
		log.info("Found {} stories for user {}", userStories.size(), userId);
		return userStories;
	}

}
