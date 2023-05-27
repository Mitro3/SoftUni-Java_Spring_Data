package com.json.productShop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.json.productShop.entities.categories.CategoryStats;
import com.json.productShop.entities.products.ProductWithoutBuyerDTO;
import com.json.productShop.entities.users.UserWithSoldProductsDTO;
import com.json.productShop.services.ProductsService;
import com.json.productShop.services.SeedService;
import com.json.productShop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductShopRunner implements CommandLineRunner {

    private final SeedService seedService;
    private final ProductsService productsService;

    private final UserService userService;

    private final Gson gson;

    @Autowired
    public ProductShopRunner(SeedService seedService, ProductsService productsService, UserService userService) {
        this.seedService = seedService;
        this.productsService = productsService;
        this.userService = userService;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void run(String... args) throws Exception {
//        this.seedService.seedAll();

//        getProductsBetweenPriceWithoutBuyer();

//        getUsersWithSoldProducts();

//        getCategoryStatistics();

        List<UserWithSoldProductsDTO> usersOrderByCount = this.userService.getUsersWithSoldProductsOrderByCount();

        String json = this.gson.toJson(usersOrderByCount);
        System.out.println(json);
        
    }

    private void getCategoryStatistics() {
        List<CategoryStats> categoryStats = this.productsService.getCategoryStatistics();

        String json = this.gson.toJson(categoryStats);
        System.out.println(json);
    }

    private void getUsersWithSoldProducts() {
        List<UserWithSoldProductsDTO> usersWithSoldProducts = this.userService.getUsersWithSoldProducts();

        String json = this.gson.toJson(usersWithSoldProducts);

        System.out.println(json);
    }

    private void getProductsBetweenPriceWithoutBuyer() {
        List<ProductWithoutBuyerDTO> productsForSale = this.productsService.getProductsInPriceRangeForSell(500, 1000);

        String json = this.gson.toJson(productsForSale);
        System.out.println(json);
    }
}
