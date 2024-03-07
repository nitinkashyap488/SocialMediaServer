package com.clone.instagram.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clone.instagram.exception.CommentException;
import com.clone.instagram.exception.PostException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Comments;
import com.clone.instagram.modal.User;
import com.clone.instagram.service.CommentService;
import com.clone.instagram.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private static final Logger log = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@PostMapping("/create/{postId}")
	public ResponseEntity<Comments> createComment(@RequestBody Comments comment, @PathVariable("postId") Integer postId,
			@RequestHeader("Authorization") String token) throws PostException, UserException {
		User user = userService.findUserProfile(token);
		Comments createdComment = commentService.createComment(comment, postId, user.getId());
		log.info("Comment created: {}", createdComment);
		return ResponseEntity.ok(createdComment);
	}

	@PutMapping("/like/{commentId}")
	public ResponseEntity<Comments> likeComment(@PathVariable Integer commentId,
			@RequestHeader("Authorization") String token) throws UserException, CommentException {
		User user = userService.findUserProfile(token);
		Comments likedComment = commentService.likeComment(commentId, user.getId());
		log.info("Liked comment: {}", likedComment);
		return ResponseEntity.ok(likedComment);
	}

	@PutMapping("/unlike/{commentId}")
	public ResponseEntity<Comments> unlikeComment(@RequestHeader("Authorization") String token,
			@PathVariable Integer commentId) throws UserException, CommentException {
		User user = userService.findUserProfile(token);
		Comments unlikedComment = commentService.unlikeComment(commentId, user.getId());
		log.info("Unliked comment: {}", unlikedComment);
		return ResponseEntity.ok(unlikedComment);
	}
}
