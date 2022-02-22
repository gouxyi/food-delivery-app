package com.cogent.fooddeliveryapp.service;

import java.util.List;
import java.util.Optional;

import com.cogent.fooddeliveryapp.dto.Role;

public interface RoleService {
	public Role addRole(Role role);
	public Optional<Role> getRoleById(long id);
	public List<Role> getAllRoles();
	public String deleteRoleById(long id);
	public Role updateRole(Role role);
}
