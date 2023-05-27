package softuni.exam.models.dto;

import softuni.exam.models.entity.ApartmentType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportApartmentDTO {

    @XmlElement(name = "apartmentType")
    @NotNull
    private ApartmentType apartmentType;

    @XmlElement
    @DecimalMin(value = "40.00")
    private Double area;

    @XmlElement
    @NotNull
    private String town;

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public Double getArea() {
        return area;
    }

    public String getTown() {
        return town;
    }
}

