package com.cogent.fooddeliveryapp.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode(exclude = {"addresses","roles"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"addresses","roles"})
@Table(name = "user_tbl",
		uniqueConstraints = @UniqueConstraint(columnNames = "email"))
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email"),
//		@UniqueConstraint(columnNames = "name")})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String email;
	
	private String password;
	
	private String name;
	
	@JsonFormat(pattern = "MM-dd-yyyy")
	private LocalDate doj = LocalDate.now();
	
	// OnetoMany Relation
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Address> addresses;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "id"))
	//private Set<Role> roles;
	public Set<Role> roles = new HashSet<>();
}
