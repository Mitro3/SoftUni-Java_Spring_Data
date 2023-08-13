package com.example.football.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stat")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportStatDTO {
    @XmlElement
    @Positive
    private double shooting;

    @XmlElement
    @Positive
    private double passing;


    @XmlElement
    @Positive
    private double endurance;

    public double getShooting() {
        return shooting;
    }

    public double getPassing() {
        return passing;
    }

    public double getEndurance() {
        return endurance;
    }
}
