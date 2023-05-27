package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCountryDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final Path COUNTRY_JSON_PATH = Path.of("src/main/resources/files/json/countries.json");

    private final String INVALID_COUNTRY_MESSAGE = "Invalid country";

    private final Validator validator;

    private final ModelMapper modelMapper;

    private final Gson gson;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Validator validator, ModelMapper modelMapper, Gson gson) {
        this.countryRepository = countryRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(COUNTRY_JSON_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        String json = this.readCountriesFromFile();
        ImportCountryDTO[] countryDTOs = this.gson.fromJson(json, ImportCountryDTO[].class);

        return Arrays.stream(countryDTOs)
                .map(dto -> importTeam(dto))
                .collect(Collectors.joining("\n"));
    }

    private String importTeam(ImportCountryDTO dto) {
        Set<ConstraintViolation<ImportCountryDTO>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return INVALID_COUNTRY_MESSAGE;
        }

        Optional<Country> optionalCountry = this.countryRepository.findByCountryName(dto.getCountryName());

        if (optionalCountry.isPresent()) {
            return INVALID_COUNTRY_MESSAGE;
        }

        Country country = this.modelMapper.map(dto, Country.class);
        this.countryRepository.save(country);

        return String.format("Successfully imported country %s - %s", country.getName(), country.getCurrency());
    }
}
