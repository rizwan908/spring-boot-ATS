package com.venturedive.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.venturedive.login.entity.User;
import com.venturedive.login.service.LoginService;
import com.venturedive.login.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LoginController {

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {

		if (user.getUsername() == null || user.getPassword() == null) {
			return new ResponseEntity<String>("username or password missing", HttpStatus.BAD_REQUEST);
		}

		boolean result = loginService.validateUser(user);

		if (!result) {
			return new ResponseEntity<String>("user not valid", HttpStatus.BAD_REQUEST);

		}

		String token = jwtUtil.generateToken(user.getUsername());
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}
}
