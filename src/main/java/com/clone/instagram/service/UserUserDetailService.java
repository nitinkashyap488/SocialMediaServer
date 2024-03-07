package com.clone.instagram.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clone.instagram.repository.UserRepository;

@Service
public class UserUserDetailService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(UserUserDetailService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<com.clone.instagram.modal.User> optionalUser = userRepository.findByEmail(username);
		com.clone.instagram.modal.User user = optionalUser.orElseThrow(() -> {
			log.error("User not found with username: {}", username);
			return new UsernameNotFoundException("User not found with username: " + username);
		});

		List<GrantedAuthority> authorities = new ArrayList<>();

		log.info("User found with username: {}", username);

		return new User(user.getEmail(), user.getPassword(), authorities);
	}
}
