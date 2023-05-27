package com.json.productShop.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.json.productShop.entities.categories.Category;
import com.json.productShop.entities.products.Product;
import com.json.productShop.entities.categories.CategoryImportDTO;
import com.json.productShop.entities.products.ProductImportDTO;
import com.json.productShop.entities.users.User;
import com.json.productShop.entities.users.UserImportDTO;
import com.json.productShop.repositories.CategoryRepository;
import com.json.productShop.repositories.ProductRepository;
import com.json.productShop.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.json.productShop.constants.Path.*;

@Service
public class SeedServiceImpl implements SeedService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public SeedServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void seedUsers() throws FileNotFoundException {
        FileReader fileReader = new FileReader(USERS_JSON_PATH);
        UserImportDTO[] userImportDTOS = this.gson.fromJson(fileReader, UserImportDTO[].class);

        List<User> users = Arrays.stream(userImportDTOS)
                .map(DTO -> this.modelMapper.map(DTO, User.class))
                .collect(Collectors.toList());

        this.userRepository.saveAll(users);
    }

    @Override
    public void seedProducts() throws FileNotFoundException {
        FileReader fileReader = new FileReader(PRODUCTS_JSON_PATH);
        ProductImportDTO[] productImportDTOS = this.gson.fromJson(fileReader, ProductImportDTO[].class);

        List<Product> products = Arrays.stream(productImportDTOS)
                .map(DTO -> this.modelMapper.map(DTO, Product.class))
                .map(this::setRandomSeller)
                .map(this::setRandomBuyer)
                .map(this::setRandomCategory)
                .collect(Collectors.toList());

        this.productRepository.saveAll(products);
    }

    private Product setRandomCategory(Product product) {
        Random random = new Random();
        long categoriesCount = this.categoryRepository.count();

        int count = random.nextInt((int) categoriesCount);

        Set<Category> categories = new HashSet<>();

        for (int i = 0; i < count; i++) {
            int randomId = random.nextInt((int) categoriesCount) + 1;

            Optional<Category> randomCategory = this.categoryRepository.findById(randomId);

            categories.add(randomCategory.get());
        }

        product.setCategories(categories);

        return product;
    }

    private Product setRandomBuyer(Product product) {
        if (product.getPrice().compareTo(BigDecimal.valueOf(944)) > 0) {
            return product;
        }

        Optional<User> buyer = getRandomUSer();

        product.setBuyer(buyer.get());

        return product;
    }

    private Product setRandomSeller(Product product) {
        Optional<User> seller = getRandomUSer();

        product.setSeller(seller.get());

        return product;
    }

    private Optional<User> getRandomUSer() {
        long usersCount = this.userRepository.count();

        int randomUserId = new Random().nextInt((int) usersCount) + 1;

        Optional<User> seller = this.userRepository.findById(randomUserId);
        return seller;
    }

    @Override
    public void seedCategories() throws FileNotFoundException {
        FileReader fileReader = new FileReader(CATEGORIES_JSON_PATH);
        CategoryImportDTO[] categoryImportDTOS = this.gson.fromJson(fileReader, CategoryImportDTO[].class);

        List<Category> categories = Arrays.stream(categoryImportDTOS)
                .map(DTO -> this.modelMapper.map(DTO, Category.class))
                .collect(Collectors.toList());

        this.categoryRepository.saveAll(categories);
    }
}
