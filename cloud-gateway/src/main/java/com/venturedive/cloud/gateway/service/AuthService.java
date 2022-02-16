package com.venturedive.cloud.gateway.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class AuthService implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        return WebClient.builder().baseUrl("http://localhost:9001").build().get().uri("/get/" + username).retrieve()
                .bodyToMono(UserDetails.class);
    }

}
