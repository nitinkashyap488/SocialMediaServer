package com.clone.instagram.modal;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotBlank;

import com.clone.instagram.dto.UserDto;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comments {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "user_id"))
	@NotNull
	private UserDto userDto;

	@NotBlank
	private String content;

	@ElementCollection
	@Embedded
	private Set<UserDto> likedByUsers = new HashSet<>();

	private LocalDateTime createdAt;
}
