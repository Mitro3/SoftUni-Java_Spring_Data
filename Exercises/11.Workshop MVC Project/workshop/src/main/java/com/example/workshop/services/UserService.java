package com.example.workshop.services;

import com.example.workshop.models.users.User;
import com.example.workshop.models.users.dtos.LoginDTO;
import com.example.workshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(LoginDTO loginDTO) {
        return this.userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
    }
}
