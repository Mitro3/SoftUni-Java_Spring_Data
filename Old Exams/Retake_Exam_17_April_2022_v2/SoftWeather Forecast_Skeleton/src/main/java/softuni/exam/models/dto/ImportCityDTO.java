package softuni.exam.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ImportCityDTO {

    @Size(min = 2, max = 60)
    @NotNull
    private String cityName;

    @Size(min = 2)
    private String description;

    @Min(500)
    private int population;

    private Long country;

    public String getCityName() {
        return cityName;
    }

    public String getDescription() {
        return description;
    }

    public int getPopulation() {
        return population;
    }

    public Long getCountry() {
        return country;
    }
}
