package com.cogent.fooddeliveryapp.payload.reponse;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.cogent.fooddeliveryapp.payload.request.Address;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// to build customize response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	@NotBlank // specifically design for string
	private String email;

	@NotBlank
	private String name;
	//@NotEmpty // specifically design for list
	private Set<Address> address;
	
	@JsonFormat(pattern = "MM-dd-yyyy") // for interview purpose
	private LocalDate doj;
	
	@NotEmpty
	private Set<String> roles;
}
