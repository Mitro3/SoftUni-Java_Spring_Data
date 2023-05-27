package com.example.xml_exercise.productShop.services;


import com.example.xml_exercise.productShop.entities.categories.Category;
import com.example.xml_exercise.productShop.entities.categories.CategoriesImportDTO;
import com.example.xml_exercise.productShop.entities.products.Product;
import com.example.xml_exercise.productShop.entities.products.ProductsImportDTO;
import com.example.xml_exercise.productShop.entities.users.User;
import com.example.xml_exercise.productShop.entities.users.UsersImportDTO;
import com.example.xml_exercise.productShop.repositories.CategoryRepository;
import com.example.xml_exercise.productShop.repositories.ProductRepository;
import com.example.xml_exercise.productShop.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.xml_exercise.productShop.constants.Path.*;

@Service
public class SeedServiceImpl implements SeedService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Autowired
    public SeedServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.mapper = new ModelMapper();
    }

    @Override
    public void seedUsers() throws FileNotFoundException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(UsersImportDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        UsersImportDTO usersDTOs = (UsersImportDTO) unmarshaller.unmarshal(new FileReader(USERS_XML_PATH));

        List<User> entities = usersDTOs.getUsers()
                .stream()
                .map(dto -> this.mapper.map(dto, User.class))
                .collect(Collectors.toList());

        this.userRepository.saveAll(entities);
    }

    @Override
    public void seedProducts() throws FileNotFoundException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(ProductsImportDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        ProductsImportDTO importDTOs = (ProductsImportDTO) unmarshaller.unmarshal(new FileReader(PRODUCTS_XML_PATH));

        List<Product> entities = importDTOs.getProducts().stream()
                .map(dto -> new Product(dto.getName(), dto.getPrice()))
                .map(this::setRandomCategory)
                .map(this::setRandomSeller)
                .map(this::setRandomBuyer)
                .collect(Collectors.toList());

        this.productRepository.saveAll(entities);
    }


    @Override
    public void seedCategories() throws FileNotFoundException, JAXBException {
        JAXBContext context = JAXBContext.newInstance(CategoriesImportDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        CategoriesImportDTO importDTOs = (CategoriesImportDTO) unmarshaller.unmarshal(new FileReader(CATEGORIES_XML_PATH));

        List<Category> entities = importDTOs.getCategories().stream()
                .map(cn -> new Category(cn.getName()))
                .collect(Collectors.toList());

        this.categoryRepository.saveAll(entities);
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

}
