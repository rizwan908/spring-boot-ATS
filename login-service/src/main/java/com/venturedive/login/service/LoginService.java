package com.venturedive.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venturedive.login.entity.User;
import com.venturedive.login.repository.UserRepository;

@Service
public class LoginService {

	@Autowired
	private UserRepository userRepository;

	public boolean validateUser(User user) {
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()) != null;
	}
}
