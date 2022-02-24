package com.cogent.fooddeliveryapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.IdNotFoundException;
import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.dto.User;
import com.cogent.fooddeliveryapp.enums.ERole;
import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.payload.reponse.UserResponse;
import com.cogent.fooddeliveryapp.payload.request.SignupRequest;
import com.cogent.fooddeliveryapp.repository.RoleRepository;
import com.cogent.fooddeliveryapp.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest){
		// can u create user object?
		// can you initialized the values based on the SignupRequest object?
		
		Set<Role> roles = new HashSet<>();
		if(signupRequest.getRoles() == null) {
			Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
					.orElseThrow(()-> new IdNotFoundException("RoleId not found exception"));
					roles.add(userRole);
		}
		else {
		signupRequest.getRoles().forEach(e ->{
			switch (e) {
			case "user":
				Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
				.orElseThrow(()-> new IdNotFoundException("RoleId not found exception"));
				roles.add(userRole);
				break;
			case "admin":
				Role userAdmin = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
				.orElseThrow(()-> new IdNotFoundException("RoleId not found exception"));
				roles.add(userAdmin);
				break;
				
			default:
				break;
			}
		});
		}
		
		User user = new User();
		
		Set<Address> addresses = new HashSet<>();
		signupRequest.getAddress().forEach(e ->{
			Address address = new Address();
			address.setHouseNumber(e.getHouseNumber());
			address.setStreet(e.getStreet());
			address.setCity(e.getCity());
			address.setState(e.getState());
			address.setCountry(e.getCountry());
			address.setUser(user);
			address.setZip(e.getZip());
			addresses.add(address);
		});
		
		user.setAddresses(addresses);
		user.setEmail(signupRequest.getEmail());
		user.setUsername(signupRequest.getUsername());
		user.setPassword(signupRequest.getPassword());
		user.setRoles(roles);
		user.setDoj(signupRequest.getDoj());
		
		User user2 = userService.addUser(user);
		
		return ResponseEntity.status(201).body(user2);
	}
	
	@GetMapping(value = "/")
	public ResponseEntity<?> getAllUsers() {
		List<User> list = userService.getAllUsers();
		List<UserResponse> userResponses = new ArrayList<>();
		list.forEach(e ->{
			UserResponse userResponse = new UserResponse();
			userResponse.setEmail(e.getEmail());
			userResponse.setName(e.getUsername());
			userResponse.setDoj(e.getDoj());
			
			Set<String> roles = new HashSet<>();
			e.getRoles().forEach(e2 ->{
				roles.add(e2.getRoleName().name());
			});
			userResponse.setRoles(roles);
			Set<com.cogent.fooddeliveryapp.payload.request.Address> addresses = new HashSet<>();
			e.getAddresses().forEach(e3->{
				com.cogent.fooddeliveryapp.payload.request.Address address2 = new com.cogent.fooddeliveryapp.payload.request.Address();
				address2.setHouseNumber(e3.getHouseNumber());
				address2.setCity(e3.getCity());
				address2.setCountry(e3.getCountry());
				address2.setState(e3.getState());
				address2.setStreet(e3.getStreet());
				address2.setZip(e3.getZip());
				addresses.add(address2);
			});
			userResponse.setAddress(addresses);
			userResponses.add(userResponse);
		});
		if(userResponses.size()>0) {
			return ResponseEntity.ok(userResponses);
		} else {
			throw new NoDataFoundException("no users are there");
		}
	}
	
	@GetMapping("/{id}") // id value will pass through url
	public ResponseEntity<?> getUserById(@PathVariable("id") long id){
		User user = userService.getUserById(id).orElseThrow(()-> new NoDataFoundException("data are not available"));
        //DTO ===> UserResponse()
		UserResponse userResponse = new UserResponse();
		userResponse.setEmail(user.getEmail());
		userResponse.setName(user.getUsername());
		userResponse.setDoj(user.getDoj());
		
		Set<String> roles = new HashSet<>();
		user.getRoles().forEach(e2 ->{
			roles.add(e2.getRoleName().name());
		});
		userResponse.setRoles(roles);
		Set<com.cogent.fooddeliveryapp.payload.request.Address> addresses = new HashSet<>();
		user.getAddresses().forEach(e3->{
			com.cogent.fooddeliveryapp.payload.request.Address address2 = new com.cogent.fooddeliveryapp.payload.request.Address();
			address2.setHouseNumber(e3.getHouseNumber());
			address2.setCity(e3.getCity());
			address2.setCountry(e3.getCountry());
			address2.setState(e3.getState());
			address2.setStreet(e3.getStreet());
			address2.setZip(e3.getZip());
			addresses.add(address2);
		});
		userResponse.setAddress(addresses);
		return ResponseEntity.status(200).body(userResponse);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateEmployee(@RequestBody User newUser, @PathVariable("id") Long id) {
		
		
		User user1 = userService.getUserById(id).map(user -> {
			user.setEmail(newUser.getEmail());
			user.setPassword(newUser.getPassword());
			user.setAddresses(user.getAddresses());
			return userService.addUser(user);
		}).orElseThrow(()-> new NoDataFoundException("data are not available"));
		return ResponseEntity.status(200).body(user1);
		
//		if (userService.existsById(id)) {
//			// success part
//			// if exists then delete it
//			User user = userService.getUserById(id).orElseThrow(()-> new NoDataFoundException("data are not available"));
//			UserResponse userResponse = new UserResponse();
//			userResponse.setEmail(newUser.getEmail());
//			userResponse.setName(newUser.getName());
//			userResponse.setDoj(newUser.getDoj());
//
//			Set<com.cogent.fooddeliveryapp.payload.request.Address> addresses = new HashSet<>();
//			newUser.getAddresses().forEach(e4->{
//				com.cogent.fooddeliveryapp.payload.request.Address address2 = new com.cogent.fooddeliveryapp.payload.request.Address();
//				address2.setHouseNumber(e4.getHouseNumber());
//				address2.setCity(e4.getCity());
//				address2.setCountry(e4.getCountry());
//				address2.setState(e4.getState());
//				address2.setStreet(e4.getStreet());
//				address2.setZip(e4.getZip());
//				addresses.add(address2);
//			});
//			userResponse.setAddress(addresses);
//			
//			return ResponseEntity.status(200).body(userResponse);
//		} 
//		else {
//			// failure part
//			// if not then throw exception
//			throw new NoDataFoundException("record not found");
//		}
		
	}
	
	@DeleteMapping("/{id}") // id value will pass through url
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id){
		
		// check userid exists or not
		if (userService.existsById(id)) {
			// success part
			// if exists then delete it
			userService.deleteUserById(id);
			return ResponseEntity.noContent().build();
		} 
		else {
			// failure part
			// if not then throw exception
			throw new NoDataFoundException("record not found");
		}
		
	}
}
