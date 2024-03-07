package com.clone.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserDto {
	private String username;
	private String name;
	private String userImage;
	private String email;
	private Integer id;
}
