package com.clone.instagram.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clone.instagram.exception.PostException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Post;
import com.clone.instagram.modal.User;
import com.clone.instagram.response.MessageResponse;
import com.clone.instagram.service.PostService;
import com.clone.instagram.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private static final Logger log = LoggerFactory.getLogger(PostController.class);

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token)
			throws UserException {
		User user = userService.findUserProfile(token);
		Post createdPost = postService.createPost(post, user.getId());

		log.info("Created post with caption: {}", post.getCaption());

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
	}

	@GetMapping("/all/{userId}")
	public ResponseEntity<List<Post>> findPostsByUserId(@PathVariable("userId") Integer userId) throws UserException {
		List<Post> posts = postService.findPostByUserId(userId);
		return ResponseEntity.ok(posts);
	}

	@GetMapping("/following/{userIds}")
	public ResponseEntity<List<Post>> findAllPostsByUserIds(@PathVariable("userIds") List<Integer> userIds)
			throws PostException, UserException {
		log.info("UserIds: {}", userIds);
		List<Post> posts = postService.findAllPostsByUserIds(userIds);
		return ResponseEntity.ok(posts);
	}

	@GetMapping("/")
	public ResponseEntity<List<Post>> findAllPosts() throws PostException {
		List<Post> posts = postService.findAllPosts();
		return ResponseEntity.ok(posts);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Post> findPostById(@PathVariable Integer postId) throws PostException {
		Post post = postService.findPostById(postId);
		return ResponseEntity.ok(post);
	}

	@PutMapping("/like/{postId}")
	public ResponseEntity<Post> likePost(@PathVariable("postId") Integer postId,
			@RequestHeader("Authorization") String token) throws UserException, PostException {
		User user = userService.findUserProfile(token);
		Post updatedPost = postService.likePost(postId, user.getId());
		return ResponseEntity.ok(updatedPost);
	}

	@PutMapping("/unlike/{postId}")
	public ResponseEntity<Post> unLikePost(@PathVariable("postId") Integer postId,
			@RequestHeader("Authorization") String token) throws UserException, PostException {
		User reqUser = userService.findUserProfile(token);
		Post updatedPost = postService.unlikePost(postId, reqUser.getId());
		return ResponseEntity.ok(updatedPost);
	}

	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<MessageResponse> deletePost(@PathVariable Integer postId,
			@RequestHeader("Authorization") String token) throws UserException, PostException {
		User reqUser = userService.findUserProfile(token);
		String message = postService.deletePost(postId, reqUser.getId());
		return ResponseEntity.ok(new MessageResponse(message));
	}

	@PutMapping("/save_post/{postId}")
	public ResponseEntity<MessageResponse> savePost(@RequestHeader("Authorization") String token,
			@PathVariable Integer postId) throws UserException, PostException {
		User user = userService.findUserProfile(token);
		String message = postService.savePost(postId, user.getId());
		return ResponseEntity.ok(new MessageResponse(message));
	}

	@PutMapping("/unsave_post/{postId}")
	public ResponseEntity<MessageResponse> unSavePost(@RequestHeader("Authorization") String token,
			@PathVariable Integer postId) throws UserException, PostException {
		User user = userService.findUserProfile(token);
		String message = postService.unsavePost(postId, user.getId());
		return ResponseEntity.ok(new MessageResponse(message));
	}
}
