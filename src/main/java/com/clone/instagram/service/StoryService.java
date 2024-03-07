package com.clone.instagram.service;

import java.util.List;

import com.clone.instagram.exception.StoryException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Story;

public interface StoryService {

	public Story createStory(Story story,Integer userId) throws UserException;
	
	public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException;
	
	
}
