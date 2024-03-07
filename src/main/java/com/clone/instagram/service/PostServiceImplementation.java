package com.clone.instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clone.instagram.dto.UserDto;
import com.clone.instagram.exception.PostException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Post;
import com.clone.instagram.modal.User;
import com.clone.instagram.repository.PostRepository;
import com.clone.instagram.repository.UserRepository;

@Service
public class PostServiceImplementation implements PostService {

	private static final Logger log = LoggerFactory.getLogger(PostServiceImplementation.class);

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Override
	public Post createPost(Post post, Integer userId) throws UserException {
		User user = userService.findUserById(userId);
		log.info("User found with id: {}", userId);

		UserDto userDto = getUserDtoFromUser(user);

		post.setUser(userDto);
		post.setCreatedAt(LocalDateTime.now());

		Post createdPost = postRepository.save(post);
		log.info("Post created with id: {}", createdPost.getId());

		return createdPost;
	}

	@Override
	public List<Post> findPostByUserId(Integer userId) throws UserException {
		List<Post> posts = postRepository.findByUserId(userId);
		log.info("Found {} posts for user with id: {}", posts.size(), userId);
		if (posts.isEmpty()) {
			throw new UserException("This user doesn't have any posts");
		}
		return posts;
	}

	@Override
	public Post findPostById(Integer postId) throws PostException {
		Optional<Post> optionalPost = postRepository.findById(postId);
		if (optionalPost.isPresent()) {
			return optionalPost.get();
		}
		throw new PostException("Post not found with id: " + postId);
	}

	@Override
	public List<Post> findAllPosts() throws PostException {
		List<Post> posts = postRepository.findAll();
		log.info("Found {} posts in total", posts.size());
		if (posts.isEmpty()) {
			throw new PostException("No posts found");
		}
		return posts;
	}

	@Override
	public Post likePost(Integer postId, Integer userId) throws UserException, PostException {
		User user = userService.findUserById(userId);
		log.info("User found with id: {}", userId);

		Post post = findPostById(postId);
		UserDto userDto = getUserDtoFromUser(user);

		if (!post.getLikedByUsers().contains(userDto)) {
			post.getLikedByUsers().add(userDto);
			postRepository.save(post);
			log.info("Post liked by user with id: {}", userId);
		}

		return post;
	}

	@Override
	public Post unlikePost(Integer postId, Integer userId) throws UserException, PostException {
		User user = userService.findUserById(userId);
		log.info("User found with id: {}", userId);

		Post post = findPostById(postId);
		UserDto userDto = getUserDtoFromUser(user);

		if (post.getLikedByUsers().remove(userDto)) {
			postRepository.save(post);
			log.info("Post unliked by user with id: {}", userId);
		}

		return post;
	}

	@Override
	public String deletePost(Integer postId, Integer userId) throws UserException, PostException {
		Post post = findPostById(postId);
		log.info("Post found with id: {}", postId);

		User user = userService.findUserById(userId);
		log.info("User found with id: {}", userId);

		if (post.getUser().getId().equals(userId)) {
			postRepository.deleteById(postId);
			log.info("Post deleted with id: {}", postId);
			return "Post deleted successfully";
		}

		throw new PostException("You don't have access to delete this post");
	}

	@Override
	public List<Post> findAllPostsByUserIds(List<Integer> userIds) throws PostException, UserException {
		List<Post> posts = postRepository.findAllPostByUserIds(userIds);
		log.info("Found {} posts for user ids: {}", posts.size(), userIds);
		if (posts.isEmpty()) {
			throw new PostException("No posts available for the given user ids");
		}
		return posts;
	}

	@Override
	public String savePost(Integer postId, Integer userId) throws PostException, UserException {
		Post post = findPostById(postId);
		log.info("Post found with id: {}", postId);

		User user = userService.findUserById(userId);
		log.info("User found with id: {}", userId);

		if (!user.getSavedPost().contains(post)) {
			user.getSavedPost().add(post);
			userRepository.save(user);
			log.info("Post saved by user with id: {}", userId);
		}

		return "Post saved successfully";
	}

	@Override
	public String unsavePost(Integer postId, Integer userId) throws PostException, UserException {
		Post post = findPostById(postId);
		log.info("Post found with id: {}", postId);

		User user = userService.findUserById(userId);
		log.info("User found with id: {}", userId);

		if (user.getSavedPost().remove(post)) {
			userRepository.save(user);
			log.info("Post unsaved by user with id: {}", userId);
		}

		return "Post unsaved successfully";
	}

	private UserDto getUserDtoFromUser(User user) {
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		return userDto;
	}

}
