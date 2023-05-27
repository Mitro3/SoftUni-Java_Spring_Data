package com.json.productShop.entities.users;

import com.json.productShop.entities.products.SoldProductsDTO;

import java.util.List;

public class UserWithSoldProductsDTO {
    private String firstName;
    private String lastName;
    private List<SoldProductsDTO> itemsBought;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<SoldProductsDTO> getItemsBought() {
        return itemsBought;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setItemsBought(List<SoldProductsDTO> itemsBought) {
        this.itemsBought = itemsBought;
    }
}
