package com.cogent.fooddeliveryapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cogent.fooddeliveryapp.advice.ControllerAdvice;
import com.cogent.fooddeliveryapp.security.jwt.AuthEntryPoint;
import com.cogent.fooddeliveryapp.security.jwt.AuthTokenFilter;
import com.cogent.fooddeliveryapp.security.service.UserDetailsServiceImpl;

@Configuration
//it will have only security related configurations
@EnableWebSecurity // it will make sure that security environment is enabled. 
@EnableGlobalMethodSecurity(prePostEnabled = true)  // pre/post authorize can be accessed by setting true
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	// private AuthEntryPoint unauthorizedHandler;
	private AuthEntryPoint unauthorizedHandler;
	// private ControllerAdvice unauthorizedHandler;
	

	@Bean // to have customized object as per the requirement
	// Scope("prototype")
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
		
		authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// core part security ----> we can restrict the access of end points through this configuration
		// we can set unauthorized access through this. 
		// we can provide direct go access for signup and signin (authorizing the resource).
		// applying token validation for end points
		// CORS : Cross-origin resource sharing (CORS) -> support for cross platform 
		
		// code for handling the endpoints
		// Enable CORS and disable CSRF
		http.cors()
		.and()
		// we used JWT, so we don't need csrf
		.csrf().disable()
		// Set unauthorized requests exception handler
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
		.and()
		// Set session management to stateless
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		// Set permissions on endpoints
		// for register and login
		// Our public endpoints
		.authorizeHttpRequests().antMatchers("/api/auth/**").permitAll()
		// Our private endpoints: for authenticated user only
		.antMatchers("/api/food/**").permitAll().anyRequest().authenticated();
		
		// code for handling the filters
		// Add JWT token filter
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		//super.configure(http);
	}
	
}
