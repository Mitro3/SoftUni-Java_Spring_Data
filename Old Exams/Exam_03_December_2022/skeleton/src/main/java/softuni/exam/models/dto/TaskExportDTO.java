package softuni.exam.models.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TaskExportDTO {

    private Long id;

    private BigDecimal price;

    private MechanicBasicInfo mechanic;

    private CarBasicInfo car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MechanicBasicInfo getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicBasicInfo mechanic) {
        this.mechanic = mechanic;
    }

    public CarBasicInfo getCar() {
        return car;
    }

    public void setCar(CarBasicInfo car) {
        this.car = car;
    }

    @Override
    public String toString() {
         String format = "Car %s %s with %dkm\n-Mechanic: %s %s - task №%d:\n --Engine: %.1f\n  ---Price: %.2f$\n";

        return String.format(format,
                car.getCarMake(),
                car.getCarModel(),
                car.getKilometers(),
                mechanic.getFirstName(),
                mechanic.getLastName(),
                id,
                car.getEngine(),
                price);
    }

    public static String format(double num) {
        DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
         return new DecimalFormat("0.00", decimalSymbols).format(num);
    }
}
