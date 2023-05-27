package com.example.xml_exercise.productShop.services;


import com.example.xml_exercise.productShop.entities.products.ExportProductsInRangeDTO;

public interface ProductsService {

    ExportProductsInRangeDTO getInRange(float i, float i1);
}
