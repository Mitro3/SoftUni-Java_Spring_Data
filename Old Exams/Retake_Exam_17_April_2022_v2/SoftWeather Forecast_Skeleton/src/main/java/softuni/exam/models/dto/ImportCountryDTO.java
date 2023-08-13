package softuni.exam.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ImportCountryDTO {

    @Size(min = 2, max = 60)
    @NotNull
    private String countryName;

    @Size(min = 2, max = 20)
    @NotNull
    private String currency;

    public String getCountryName() {
        return countryName;
    }

    public String getCurrency() {
        return currency;
    }
}
