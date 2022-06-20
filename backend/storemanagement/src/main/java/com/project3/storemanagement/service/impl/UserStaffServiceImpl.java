package com.project3.storemanagement.service.impl;

import com.project3.storemanagement.entities.RecordStatus;
import com.project3.storemanagement.entities.User;
import com.project3.storemanagement.service.UserStaffService;
import com.project3.storemanagement.exception.BadNumberException;
import com.project3.storemanagement.exception.RecordNotFoundException;
import com.project3.storemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserStaffServiceImpl implements UserStaffService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Iterable<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        if (id <= 0) {
            throw new BadNumberException("Id phải lớn hơn 0");
        }
        return userRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy người dùng"));
    }

    @Override
    public Iterable<User> listAllUsersByRecordStatus() {
        return userRepository.findByRecordStatus(RecordStatus.ACTIVE.getStatus());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        if (id <= 0) {
            throw new BadNumberException("Id phải lớn hơn 0");
        }
        User existingUser = this.getUserById(id);
        existingUser.setEmail(user.getEmail());
        //existingUser.setPassword(user.getPassword());
        existingUser.setUsername(user.getUsername());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public String deleteUser(Long id) {
        if (id <= 0) {
            throw new BadNumberException("Id phải lớn hơn 0");
        }

        User existingUser = this.getUserById(id);
        existingUser.setRecordStatus(RecordStatus.DELETED);
        userRepository.save(existingUser);
        return "Người dùng đã bị xóa";
    }

}
