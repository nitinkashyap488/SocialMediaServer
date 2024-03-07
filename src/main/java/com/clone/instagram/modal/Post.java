package com.clone.instagram.modal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clone.instagram.dto.UserDto;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String caption;

	@Column(nullable = false)
	private String image;

	private String location;

	private LocalDateTime createdAt;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "user_id"))
	@AttributeOverride(name = "email", column = @Column(name = "user_email"))
	@AttributeOverride(name = "username", column = @Column(name = "user_username"))
	private UserDto user;

	@OneToMany
	private List<Comments> comments = new ArrayList<>();

	@ElementCollection
	@Embedded
	@JoinTable(name = "likeByUsers", joinColumns = @JoinColumn(name = "user_id"))
	private Set<UserDto> likedByUsers = new HashSet<>();
}
