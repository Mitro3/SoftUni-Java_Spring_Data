package com.example.bookshopV2.services;

import com.example.bookshopV2.domain.entities.Category;

import java.util.Set;

public interface CategoryService {
    Set<Category> getRandomCategories();
}
