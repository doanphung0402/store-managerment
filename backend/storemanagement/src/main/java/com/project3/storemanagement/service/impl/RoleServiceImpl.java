package com.project3.storemanagement.service.impl;

import com.project3.storemanagement.entities.Role;
import com.project3.storemanagement.service.RoleService;
import com.project3.storemanagement.exception.RecordNotFoundException;
import com.project3.storemanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository
            .findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy vai trò tương ứng"));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository
            .findByName(name)
            .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy vai trò"));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
