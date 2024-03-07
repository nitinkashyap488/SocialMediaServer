package com.clone.instagram.modal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clone.instagram.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String username;
	private String email;
	private String name;
	private String mobile;
	private String website;
	private String bio;
	private String gender;
	private String image;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@ElementCollection
	@Embedded
	private Set<UserDto> follower = new HashSet<>();

	@ElementCollection
	@Embedded
	private Set<UserDto> following = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Story> stories = new ArrayList<>();

	@ManyToMany
	private List<Post> savedPost = new ArrayList<>();
}
