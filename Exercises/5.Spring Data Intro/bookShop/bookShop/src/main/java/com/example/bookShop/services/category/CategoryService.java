package com.example.bookShop.services.category;

import com.example.bookShop.domain.entities.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    void seedCategories(List<Category> categories);

    boolean isDataSeeded();

    Set<Category> getRandomCategories();
}
