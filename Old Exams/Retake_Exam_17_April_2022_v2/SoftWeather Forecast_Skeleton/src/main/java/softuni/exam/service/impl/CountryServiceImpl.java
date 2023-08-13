package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCountryDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static softuni.exam.util.Constants.INVALID_FORMAT;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    private static final String COUNTRY_FILE_PATH = "src/main/resources/files/json/countries.json";

    private final Gson gson;

    private final ValidationUtils validationUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ValidationUtils validationUtils, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRY_FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        ImportCountryDTO[] importCountryDTOS = this.gson.fromJson(readCountriesFromFile(), ImportCountryDTO[].class);

        List<String> result = new ArrayList<>();

        for (ImportCountryDTO importCountryDTO : importCountryDTOS) {
            result.add(importCountry(importCountryDTO));
        }

        return String.join("", result);
    }

    private String importCountry(ImportCountryDTO dto) {
        Optional<Country> optionalCountry = this.countryRepository.findByCountryName(dto.getCountryName());

        if (optionalCountry.isEmpty() && validationUtils.isValid(dto)) {
            Country country = this.modelMapper.map(dto, Country.class);
            this.countryRepository.save(country);

            String message = String.format("Successfully imported country %s - %s%n",
                    dto.getCountryName(), dto.getCurrency());

            return message;

        } else {
            return String.format(INVALID_FORMAT, "Country");
        }
    }
}
