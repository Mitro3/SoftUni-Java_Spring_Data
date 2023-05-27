package com.example.bookShop.services.category;

import com.example.bookShop.domain.entities.Category;
import com.example.bookShop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories(List<Category> categories) {
        this.categoryRepository.saveAll(categories);
    }

    @Override
    public boolean isDataSeeded() {
        return this.categoryRepository.count() > 0;
    }

    @Override
    public Set<Category> getRandomCategories() {
        final long count = this.categoryRepository.count();

        if (count != 0) {
            long randomCategory = new Random().nextLong(1L, count);
            return Set.of(this.categoryRepository.findById(randomCategory).orElseThrow(NoSuchElementException::new));
        }

        throw new RuntimeException();
    }
}
