package com.cogent.fooddeliveryapp.dto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.cogent.fooddeliveryapp.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Role {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private long roleId;
	@NotNull
	@Enumerated(EnumType.STRING)
	private ERole roleName; 
	
//	@ManyToOne
//	private User userRoles;
	
}
