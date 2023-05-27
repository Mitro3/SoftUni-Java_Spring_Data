package com.json.productShop.repositories;

import com.json.productShop.entities.users.User;
import com.json.productShop.entities.users.UserWithSoldProductsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u " +
            "JOIN u.soldItems p " +
            "WHERE p.buyer IS NOT NULL")
    List<User> findAllWithSoldProducts();

    @Query("SELECT u FROM User u " +
            "JOIN u.soldItems p " +
            "WHERE p.buyer IS NOT NULL " +
            "ORDER BY size(u.soldItems) DESC, u.lastName ASC")
    List<User> findAllWithSoldProductsOrderByCount();
}
