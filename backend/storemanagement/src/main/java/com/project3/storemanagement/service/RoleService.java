package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.Role;

public interface RoleService {
    Role getRoleById(Long id);

    Role getRoleByName(String name);

    Role saveRole(Role role);
}
