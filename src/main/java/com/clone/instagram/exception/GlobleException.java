package com.clone.instagram.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobleException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetails> userExceptionHandler(UserException userException, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(userException.getMessage(), request.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(PostException.class)
	public ResponseEntity<ErrorDetails> postExceptionHandler(PostException postException, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(postException.getMessage(), request.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(CommentException.class)
	public ResponseEntity<ErrorDetails> commentsExceptionHandler(CommentException commentException,
			WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(commentException.getMessage(), request.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(StoryException.class)
	public ResponseEntity<ErrorDetails> storyExceptionHandler(StoryException storyException, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(storyException.getMessage(), request.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> methodArgumentNotValidExceptionHandler(
			MethodArgumentNotValidException methodArgumentNotValidException) {
		ErrorDetails errorDetails = new ErrorDetails(
				methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage(),
				"validation error", LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> otherExceptionHandler(Exception exception, WebRequest request) {

		ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);

	}
}
