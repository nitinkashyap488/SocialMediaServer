package com.clone.instagram.service;

import java.util.List;

import com.clone.instagram.exception.PostException;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.Post;

public interface PostService {

	public Post createPost(Post post, Integer userId) throws UserException;

	public String deletePost(Integer postId, Integer userId) throws UserException, PostException;

	public List<Post> findPostByUserId(Integer userId) throws UserException;

	public Post findPostById(Integer postId) throws PostException;

	public List<Post> findAllPosts() throws PostException;

	public List<Post> findAllPostsByUserIds(List<Integer> userIds) throws PostException, UserException;

	public String savePost(Integer postId, Integer userId) throws PostException, UserException;

	public String unsavePost(Integer postId, Integer userId) throws PostException, UserException;

	public Post likePost(Integer postId, Integer userId) throws UserException, PostException;

	public Post unlikePost(Integer postId, Integer userId) throws UserException, PostException;
}
