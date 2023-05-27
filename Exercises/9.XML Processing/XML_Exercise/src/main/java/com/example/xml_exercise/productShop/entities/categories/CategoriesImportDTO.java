package com.example.xml_exercise.productShop.entities.categories;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriesImportDTO {

    @XmlElement(name = "category")
    private List<CategoryNameDTO> categories;

    public CategoriesImportDTO() {
        this.categories = new ArrayList<>();
    }

    public List<CategoryNameDTO> getCategories() {
        return categories;
    }
}
