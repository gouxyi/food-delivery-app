package com.cogent.fooddeliveryapp.payload.request;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SignupRequest {
	
	@NotBlank // specifically design for string
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String username;
	@NotEmpty // specifically design for list
	private Set<Address> address;
	
	@JsonFormat(pattern = "MM-dd-yyyy") // for interview purpose
	private LocalDate doj;
	
	@NotEmpty
	private Set<String> roles;
	
}

// User Role
// Admin Role
// Role Enum
// FoodType enum
// Role DTO : role Id and RoleName (enum)
// repo --> role
// roleService --> and its impl
