package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "townName")
@XmlAccessorType(XmlAccessType.FIELD)
public class NameDTO {

    @XmlElement
    private String name;

    public String getName() {
        return name;
    }
}
