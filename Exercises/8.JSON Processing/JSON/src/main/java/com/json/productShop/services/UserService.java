package com.json.productShop.services;

import com.json.productShop.entities.users.UserWithSoldProductsDTO;

import java.util.List;

public interface UserService {
    List<UserWithSoldProductsDTO> getUsersWithSoldProducts();
    List<UserWithSoldProductsDTO> getUsersWithSoldProductsOrderByCount();
}
