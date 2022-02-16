package com.venturedive.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.venturedive.login.dao.UserDto;
import com.venturedive.login.entity.User;
import com.venturedive.login.repository.UserRepository;
import com.venturedive.login.service.UserService;
import com.venturedive.login.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDto userDto) {
		if (userDto.getUsername() == null || userDto.getPassword() == null) {
			String message = "username or password missing";
			LoginController.log.info(message);
			return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
		}

		User user = userRepository.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
		if (user == null) {
			String message = "user not valid";
			LoginController.log.info(message);
			return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
		}

		try {
			String token = jwtUtil.generateToken(user);
			return new ResponseEntity<String>(token, HttpStatus.OK);
		} catch (JsonProcessingException e) {
			String message = "failed to authenticate user";
			LoginController.log.error(message, e);
			return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get/{username}")
	public ResponseEntity<UserDetails> authenticate(@RequestParam String username) {
		return new ResponseEntity<UserDetails>(userService.findByUsername(username).block(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ticket')")
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return new ResponseEntity<String>("hello", HttpStatus.OK);
	}
}
