package com.project3.storemanagement.controller;


import com.project3.storemanagement.dto.RegisterRequest;
import com.project3.storemanagement.entities.User;
import com.project3.storemanagement.service.UserStaffService;
import com.project3.storemanagement.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private final UserStaffService userStaffService;

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserStaffService userStaffService) {
        this.userStaffService = userStaffService;
    }

    @GetMapping
    public Iterable<User> findAllUserByRecordStatus() {
        return userStaffService.listAllUsersByRecordStatus();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable long id) {
        return userStaffService.getUserById(id);
    }

//    @GetMapping
//    public Iterable<User> findAllUsers() {
//        return userStaffService.listAllUsers();
//    }

    @PostMapping
    public void createUserStaff(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.createUserStaffAccount(registerRequest);
    }

    @PutMapping("/{id}")
    public User updateUserStaff(@PathVariable long id, @RequestBody @Valid User user) {
        return userStaffService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUserStaff(@PathVariable long id) {
        return userStaffService.deleteUser(id);
    }
}
