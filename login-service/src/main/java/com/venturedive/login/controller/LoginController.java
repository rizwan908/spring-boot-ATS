package com.venturedive.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.venturedive.login.dao.UserDao;
import com.venturedive.login.entity.User;
import com.venturedive.login.repository.UserRepository;
import com.venturedive.login.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDao userDao) {
		if (userDao.getUsername() == null || userDao.getPassword() == null) {
			String message ="username or password missing";
			LoginController.log.info(message);
			return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
		}

		User user = userRepository.findByUsernameAndPassword(userDao.getUsername(), userDao.getPassword());
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
}
