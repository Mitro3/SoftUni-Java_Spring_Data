package com.json.productShop.repositories;

import com.json.productShop.entities.products.Product;
import com.json.productShop.entities.categories.CategoryStats;
import com.json.productShop.entities.products.ProductWithoutBuyerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT new com.json.productShop.entities.products.ProductWithoutBuyerDTO(" +
            "p.name, p.price, p.seller.firstName, p.seller.lastName)" +
            " FROM Product p" +
            " WHERE p.price > :rangeStart AND p.price < :rangeEnd AND p.buyer IS NULL" +
            " ORDER BY p.price ASC")
    List<ProductWithoutBuyerDTO> findALLByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal rangeStart, BigDecimal rangeEnd);

    @Query("SELECT new com.json.productShop.entities.categories.CategoryStats(" +
            "c.name, COUNT(p), AVG(p.price), SUM(p.price)) " +
            "FROM Product p " +
            "JOIN p.categories c " +
            "GROUP BY c")
    List<CategoryStats> getCategoryStats();
}
