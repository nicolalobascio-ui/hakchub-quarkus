package com.hackhub.services;

import com.hackhub.entity.User;
import com.hackhub.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped 
public class UserService {

    @Inject
    private UserRepository userRepository;

    public User findIdByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
}
