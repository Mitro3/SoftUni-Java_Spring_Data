package com.json.productShop.services;

import com.json.productShop.entities.categories.CategoryStats;
import com.json.productShop.entities.products.ProductWithoutBuyerDTO;

import java.util.List;

public interface ProductsService {
    List<ProductWithoutBuyerDTO> getProductsInPriceRangeForSell(float from, float to);

    List<CategoryStats> getCategoryStatistics();
}
