package com.venturedive.login.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.venturedive.login.dto.UserDto;
import com.venturedive.login.entity.Role;
import com.venturedive.login.entity.User;
import com.venturedive.login.repository.UserRepository;
import com.venturedive.login.utils.JwtUtils;

import io.jsonwebtoken.Jwts;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class LoginControllerTest {

	@Mock
	UserRepository userRepository;

	@Mock
	JwtUtils jwtUtils;

	@InjectMocks
	LoginController loginController;

	@Test
	void testLogin() throws Exception {
		UserDto userDto = new UserDto("test", "test");

		Role role = new Role();
		role.setId(1L);
		role.setName("test");

		Set<Role> roles = new HashSet<Role>();
		roles.add(role);

		User user = new User("test", "test", roles);

		Mockito.when(userRepository.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword()))
				.thenReturn(user);

		Mockito.when(jwtUtils.generateToken(user)).thenCallRealMethod();
		ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testing123");
		ReflectionTestUtils.setField(jwtUtils, "tokenValidity", 604800000L);
		String token = loginController.login(userDto).block().getBody();

		assertNotNull(token);
		assertNotEquals(token, "");
		assertNotNull(Jwts.parser().setSigningKey("testing123").parseClaimsJws(token));
		assertTrue(loginController.testEndpoint().block().getBody().equals("hello test"));
	}
}
