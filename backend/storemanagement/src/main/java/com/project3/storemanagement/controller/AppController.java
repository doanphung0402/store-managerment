package com.project3.storemanagement.controller;

import com.project3.storemanagement.dto.LoginRequest;
import com.project3.storemanagement.dto.LoginResponse;
import com.project3.storemanagement.dto.RegisterRequest;
import com.project3.storemanagement.entities.AppUserDetails;
import com.project3.storemanagement.entities.Role;
import com.project3.storemanagement.entities.User;
import com.project3.storemanagement.security.JwtTokenProvider;
import com.project3.storemanagement.service.UserStaffService;
import com.project3.storemanagement.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping
@CrossOrigin
public class AppController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserStaffService userStaffService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken((AppUserDetails) authentication.getPrincipal());
        User user = userStaffService.getUserByUsername(loginRequest.getUsername());
        Set<Role> role = user.getRoles();
        Long id = user.getId();
        return new LoginResponse(jwt, role, id);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.createAdminAccount(registerRequest);
    }

}
