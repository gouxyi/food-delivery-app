package com.cogent.fooddeliveryapp.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.cogent.fooddeliveryapp.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = 
			LoggerFactory
			.getLogger(JwtUtils.class);
	
	@Value("${com.cogent.fooddeliveryapp.jwtSecret}")
	private String jwtSecret;
	
	@Value("${com.cogent.fooddeliveryapp.jwtExpirationMs}")
	private long jwtExpirationMs;
	
	// to generate the token
	public String generateToken(Authentication authentication) {
		
		UserDetailsImpl userPricipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(userPricipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	// validation of token
	public boolean validateJwtToken(String authToken) {
		
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(authToken);
			return true;
		} catch (ExpiredJwtException e) {
			// TODO Auto-generated catch block
			logger.error("JWT token is expired:{}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			// TODO Auto-generated catch block
			logger.error("JWT token is unsuppoerted:{}", e.getMessage());
		} catch (MalformedJwtException e) {
			// TODO Auto-generated catch block
			logger.error("Invalid JWT token:{}", e.getMessage());
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			logger.error("Invalid JWT signature:{}", e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logger.error("JWT claims string is empty:{}", e.getMessage());
		}
		return false;
	}
	
	// get name from the token ==> 
	public String getUserNameFromJwtToken(String authToken) {
		
		return Jwts.parser() // compact ----> javaobject
				.setSigningKey(jwtSecret) // ---> secret key ---> encoding is done
				.parseClaimsJws(authToken) // provided actual token
				.getBody() // extracting the body content
				.getSubject(); // extracting the subject
	}
}
