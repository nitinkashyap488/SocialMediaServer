package com.clone.instagram.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtGeneratorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null) {
			SecretKey key = Keys.hmacShaKeyFor(SecurityContext.JWT_KEY.getBytes());

			String jwtToken = generateJwtToken(authentication, key);

			response.setHeader(SecurityContext.HEADER, jwtToken);
		}

		filterChain.doFilter(request, response);
	}

	private String generateJwtToken(Authentication authentication, SecretKey key) {
		Set<String> authorities = extractAuthorities(authentication.getAuthorities());

		return Jwts.builder().setIssuer("Nitin Kashyap").claim("authorities", String.join(",", authorities))
				.claim("username", authentication.getName()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + SecurityContext.EXPIRATION_TIME)).signWith(key)
				.compact();
	}

	private Set<String> extractAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authorityStrings = new HashSet<>();

		for (GrantedAuthority authority : authorities) {
			authorityStrings.add(authority.getAuthority());
		}

		return authorityStrings;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/signin");
	}
}
