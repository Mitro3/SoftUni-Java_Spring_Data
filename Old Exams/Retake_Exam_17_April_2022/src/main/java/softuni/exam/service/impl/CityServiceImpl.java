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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    private final Gson gson;

    private final Path CITY_JSON_PATH = Path.of("src/main/resources/files/json/cities.json");

    private final String INVALID_CITY_MESSAGE = "Invalid city";

    private final Validator validator;

    private final ModelMapper modelMapper;
    private final CountryRepository countryRepository;


    @Autowired
    public CityServiceImpl(CityRepository cityRepository, Gson gson, Validator validator, ModelMapper modelMapper, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.gson = gson;
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(CITY_JSON_PATH));
    }

    @Override
    public String importCities() throws IOException {
        String json = this.readCitiesFileContent();
        ImportCityDTO[] importCityDTOS = this.gson.fromJson(json, ImportCityDTO[].class);

        return Arrays.stream(importCityDTOS)
                .map(dto -> importCity(dto))
                .collect(Collectors.joining("\n"));
    }

    private String importCity(ImportCityDTO dto) {
        Set<ConstraintViolation<ImportCityDTO>> errors = this.validator.validate(dto);

            if (!errors.isEmpty()) {
                return INVALID_CITY_MESSAGE;
            }

            Optional<City> optCity = this.cityRepository.findByCityName(dto.getCityName());

            if (optCity.isPresent()) {
                    return INVALID_CITY_MESSAGE;
            }

            City city = this.modelMapper.map(dto, City.class);
            Optional<Country> optCountry = this.countryRepository.findById(dto.getCountry());
            city.setCountry(optCountry.get());
            this.cityRepository.save(city);

        return String.format("Successfully imported city %s - %d", city.getCityName(), city.getPopulation());
    }
}
