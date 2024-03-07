package com.clone.instagram.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clone.instagram.dto.UserDto;
import com.clone.instagram.exception.UserException;
import com.clone.instagram.modal.User;
import com.clone.instagram.repository.UserRepository;
import com.clone.instagram.security.JwtTokenProvider;

@Service
public class UserServiceImplementation implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public User registerUser(User user) throws UserException {
		log.info("Registering new user: {}", user.getUsername());

		if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null
				|| user.getName() == null) {
			throw new UserException("Email, password, username, and name are required");
		}

		Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
		Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());

		if (existingUserByEmail.isPresent() || existingUserByUsername.isPresent()) {
			throw new UserException(existingUserByEmail.isPresent() ? "Email Already Exist" : "Username Already Taken");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User findUserById(Integer userId) throws UserException {
		log.info("Finding user by ID: {}", userId);
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserException("User not found with ID: " + userId));
	}

	@Override
	public String followUser(Integer requesterId, Integer followUserId) throws UserException {
		log.info("Following user: {} -> {}", requesterId, followUserId);
		User requester = findUserById(requesterId);
		User followUser = findUserById(followUserId);

		UserDto requesterDto = createUserDto(requester);
		UserDto followUserDto = createUserDto(followUser);

		followUser.getFollower().add(requesterDto);
		requester.getFollowing().add(followUserDto);

		userRepository.saveAll(List.of(followUser, requester));

		return "You are now following " + followUser.getUsername();
	}

	@Override
	public String unfollowUser(Integer requesterId, Integer unfollowUserId) throws UserException {
		log.info("Unfollowing user: {} -> {}", requesterId, unfollowUserId);
		User requester = findUserById(requesterId);
		User unfollowUser = findUserById(unfollowUserId);

		UserDto requesterDto = createUserDto(requester);

		unfollowUser.getFollower().remove(requesterDto);
		userRepository.save(unfollowUser);

		return "You have unfollowed " + unfollowUser.getUsername();
	}

	@Override
	public User findUserProfile(String token) throws UserException {
		log.info("Finding user profile");
		String username = jwtTokenProvider.getClaimsFromToken(token.substring(7)).getUsername();
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new UserException("User not found with username: " + username));
	}

	@Override
	public User findUserByUsername(String username) throws UserException {
		log.info("Finding user by username: {}", username);
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UserException("User not found with username " + username));
	}

	@Override
	public List<User> findUsersByUserIds(List<Integer> userIds) {
		log.info("Finding users by IDs: {}", userIds);
		return userRepository.findAllById(userIds);
	}

	@Override
	public List<User> searchUser(String query) throws UserException {
		log.info("Searching users with query: {}", query);
		List<User> users = userRepository.findByQuery(query);
		if (users.isEmpty()) {
			throw new UserException("No users found matching query: " + query);
		}
		return users;
	}

	@Override
	public User updateUserDetails(User updatedUser, User existingUser) throws UserException {
		log.info("Updating user details for user: {}", existingUser.getUsername());
		if (!updatedUser.getId().equals(existingUser.getId())) {
			throw new UserException("You can't update another user");
		}

		if (updatedUser.getEmail() != null) {
			existingUser.setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getBio() != null) {
			existingUser.setBio(updatedUser.getBio());
		}
		if (updatedUser.getName() != null) {
			existingUser.setName(updatedUser.getName());
		}
		if (updatedUser.getUsername() != null) {
			existingUser.setUsername(updatedUser.getUsername());
		}
		if (updatedUser.getMobile() != null) {
			existingUser.setMobile(updatedUser.getMobile());
		}
		if (updatedUser.getGender() != null) {
			existingUser.setGender(updatedUser.getGender());
		}
		if (updatedUser.getWebsite() != null) {
			existingUser.setWebsite(updatedUser.getWebsite());
		}
		if (updatedUser.getImage() != null) {
			existingUser.setImage(updatedUser.getImage());
		}

//		if (updatedUser.getId() == existingUser.getId()) {
//			log.info(" u " + updatedUser.getId() + " e " + existingUser.getId());
//			throw new UserException("you can't update another user");
//		}

		return userRepository.save(existingUser);

	}

	private UserDto createUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		return userDto;
	}

}
