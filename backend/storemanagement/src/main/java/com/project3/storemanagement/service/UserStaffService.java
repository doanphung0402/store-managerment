package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.User;

public interface UserStaffService {

    User getUserById(Long id);

    Iterable<User> listAllUsersByRecordStatus();

    User getUserByUsername(String username);

    User updateUser(Long id, User user);

    String deleteUser(Long id);

    Iterable<User> listAllUsers();

}
