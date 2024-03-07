package com.clone.instagram.service;

import com.clone.instagram.exception.CommentException;
import com.clone.instagram.exception.PostException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Comments;

public interface CommentService {

	public Comments createComment(Comments comment, Integer postId, Integer userId) throws PostException, UserException;

	public Comments findCommentById(Integer commentId) throws CommentException;

	public Comments likeComment(Integer commentId, Integer userId) throws UserException, CommentException;

	public Comments unlikeComment(Integer commentId, Integer userId) throws UserException, CommentException;
}
