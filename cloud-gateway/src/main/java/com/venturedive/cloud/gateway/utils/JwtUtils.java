package com.venturedive.cloud.gateway.utils;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	@Value("${jwt.secret}")
	private String jwtSecret;

	public void validateToken(final String token) throws AuthenticationException {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
		} catch (SignatureException ex) {
			throw new AuthenticationException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new AuthenticationException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new AuthenticationException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new AuthenticationException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new AuthenticationException("JWT claims string is empty.");
		}
	}

}
