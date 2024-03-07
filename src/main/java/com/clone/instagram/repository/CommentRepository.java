package com.clone.instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.instagram.modal.Comments;

public interface CommentRepository extends JpaRepository<Comments, Integer> {

}
