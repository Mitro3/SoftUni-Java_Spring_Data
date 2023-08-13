package softuni.exam.models.dto;

import javax.validation.constraints.*;

public class PartImportDTO {

    @NotNull
    @Size(min = 2, max = 19)
    private String partName;

    @NotNull
    @DecimalMin(value = "10.0")
    @DecimalMax(value = "2000.0")
    private double price;

    @NotNull
    @Positive
    private String quantity;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
