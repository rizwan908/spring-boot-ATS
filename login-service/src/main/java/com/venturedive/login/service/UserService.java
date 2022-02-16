package com.venturedive.login.service;

import com.venturedive.login.entity.User;
import com.venturedive.login.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveUserDetailsService {

    @Autowired
    UserRepository repo;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        User user = repo.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(String.format("no user found with username %s", username));
        return Mono.just(user);
    }

}
