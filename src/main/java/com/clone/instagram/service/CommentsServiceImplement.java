package com.clone.instagram.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clone.instagram.dto.UserDto;
import com.clone.instagram.exception.CommentException;
import com.clone.instagram.exception.PostException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Comments;
import com.clone.instagram.modal.Post;
import com.clone.instagram.modal.User;
import com.clone.instagram.repository.CommentRepository;
import com.clone.instagram.repository.PostRepository;

@Service
public class CommentsServiceImplement implements CommentService {

	private static final Logger log = LoggerFactory.getLogger(CommentsServiceImplement.class);

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Override
	public Comments createComment(Comments comment, Integer postId, Integer userId)
			throws PostException, UserException {

		User user = userService.findUserById(userId);
		Post post = postService.findPostById(postId);

		comment.setUserDto(createUserDtoFromUser(user));
		comment.setCreatedAt(LocalDateTime.now());

		Comments newComment = commentRepository.save(comment);
		post.getComments().add(newComment);
		postRepository.save(post);

		log.info("Comment created: {}", newComment);

		return newComment;
	}

	@Override
	public Comments findCommentById(Integer commentId) throws CommentException {
		return commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentException("Comment not found with id: " + commentId));
	}

	@Override
	public Comments likeComment(Integer commentId, Integer userId) throws UserException, CommentException {
		User user = userService.findUserById(userId);
		Comments comment = findCommentById(commentId);

		comment.getLikedByUsers().add(createUserDtoFromUser(user));
		Comments likedComment = commentRepository.save(comment);

		log.info("Comment liked: {}", likedComment);

		return likedComment;
	}

	@Override
	public Comments unlikeComment(Integer commentId, Integer userId) throws UserException, CommentException {
		User user = userService.findUserById(userId);
		Comments comment = findCommentById(commentId);
		comment.getLikedByUsers().removeIf(likedUser -> likedUser.getId().equals(userId));
		return commentRepository.save(comment);
	}

	private UserDto createUserDtoFromUser(User user) {
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		return userDto;
	}
}
