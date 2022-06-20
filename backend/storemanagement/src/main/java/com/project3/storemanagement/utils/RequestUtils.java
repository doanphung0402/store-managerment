package com.project3.storemanagement.utils;

import com.project3.storemanagement.entities.AppUserDetails;
import com.project3.storemanagement.entities.User;
import com.project3.storemanagement.security.JwtAuthenticationFilter;
import com.project3.storemanagement.security.JwtTokenProvider;
import com.project3.storemanagement.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUtils {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    public Long getUserIdFromRequest(HttpServletRequest servletRequest) {
        String jwt = jwtAuthenticationFilter.getJwtFromRequest(servletRequest);
        if(jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid jwt token");
        }
        String username = jwtTokenProvider.getUsernameFromJwt(jwt);
        User user = ((AppUserDetails) userService.loadUserByUsername(username)).getUser();
        return user.getId();
    }
}
