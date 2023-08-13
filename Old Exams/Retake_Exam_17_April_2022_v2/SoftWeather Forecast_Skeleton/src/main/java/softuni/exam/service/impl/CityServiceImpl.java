package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCityDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.util.Constants.INVALID_FORMAT;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    private final CountryRepository countryRepository;

    private static final String CITY_FILE_PATH = "src/main/resources/files/json/cities.json";

    private final ValidationUtils validationUtils;

    private final Gson gson;

    private final ModelMapper modelMapper;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, ValidationUtils validationUtils, Gson gson, ModelMapper modelMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.validationUtils = validationUtils;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITY_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        ImportCityDTO[] importCityDTOS = this.gson.fromJson(readCitiesFileContent(), ImportCityDTO[].class);

        return Arrays.stream(importCityDTOS)
                .map(this::importCity).collect(Collectors.joining());
    }

    private String importCity(ImportCityDTO dto) {
        Optional<City> optionalCity = this.cityRepository.findByCityName(dto.getCityName());

        String message = "";

        if (optionalCity.isEmpty() && this.validationUtils.isValid(dto)) {
            City city = this.modelMapper.map(dto, City.class);

            Optional<Country> optionalCountry = this.countryRepository.findById(dto.getCountry());
            city.setCountry(optionalCountry.get());

            this.cityRepository.save(city);

            message = String.format("Successfully imported city %s - %d%n", city.getCityName(), city.getId());
        } else {
            message = String.format(INVALID_FORMAT, "City");
        }

        return message;
    }
}
