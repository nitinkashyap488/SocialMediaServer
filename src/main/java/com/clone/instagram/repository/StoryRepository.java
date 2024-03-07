package com.clone.instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clone.instagram.modal.Story;

public interface StoryRepository extends JpaRepository<Story, Integer> {

	@Query("SELECT s FROM Story s WHERE s.userDto.id = :userId")
	List<Story> findAllStoriesByUserId(@Param("userId") Integer userId);

}
