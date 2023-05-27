package com.json.productShop.services;

import com.json.productShop.entities.users.User;
import com.json.productShop.entities.users.UserWithSoldProductsDTO;
import com.json.productShop.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new ModelMapper();
    }

    @Override
    @Transactional
    public List<UserWithSoldProductsDTO> getUsersWithSoldProducts() {
        List<User> allWithSoldProducts = this.userRepository.findAllWithSoldProducts();

       return allWithSoldProducts
                .stream()
                .map(user -> this.mapper.map(user, UserWithSoldProductsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserWithSoldProductsDTO> getUsersWithSoldProductsOrderByCount() {
        List<User> all = this.userRepository.findAllWithSoldProductsOrderByCount();

        return all
                .stream()
                .map(user -> this.mapper.map(user, UserWithSoldProductsDTO.class))
                .collect(Collectors.toList());
    }
}
