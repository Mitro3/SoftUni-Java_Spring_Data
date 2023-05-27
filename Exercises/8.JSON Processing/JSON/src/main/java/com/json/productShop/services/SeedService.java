package com.json.productShop.services;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

public interface SeedService {
    void seedUsers() throws FileNotFoundException;
    void seedProducts() throws FileNotFoundException;
    void seedCategories() throws FileNotFoundException;
    default void seedAll() throws FileNotFoundException {
        seedUsers();
        seedProducts();
        seedCategories();
    }
}
