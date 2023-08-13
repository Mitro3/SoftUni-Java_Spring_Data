package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastDTO;
import softuni.exam.models.dto.ImportForecastRootDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.models.entity.DayOfWeek.SUNDAY;
import static softuni.exam.util.Constants.INVALID_FORMAT;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;

    private final CityRepository cityRepository;

    private static final String FORECAST_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";

    private final XmlParser xmlParser;

    private final ValidationUtils validationUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository, XmlParser xmlParser, ValidationUtils validationUtils, ModelMapper modelMapper) {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.xmlParser = xmlParser;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;

    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECAST_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastRootDTO importForecastRootDTO = this.xmlParser
                .fromFile(Path.of(FORECAST_FILE_PATH).toFile(), ImportForecastRootDTO.class);

        List<ImportForecastDTO> forecasts = importForecastRootDTO.getForecasts();

        List<String> result = new ArrayList<>();

//        return forecasts.stream()
//                .map(this::importForecast).collect(Collectors.joining());
        for (ImportForecastDTO forecast : forecasts) {
            result.add(importForecast(forecast));
        }

        return String.join("\n", result);
    }

    private String importForecast(ImportForecastDTO dto) {
        Optional<Forecast> optionalForecast = this.forecastRepository
                .findByDayOfWeekAndCityId(dto.getDayOfWeek(), dto.getCityId());

        String message = "";

        if (optionalForecast.isEmpty() && validationUtils.isValid(dto)) {
            Forecast forecast = this.modelMapper.map(dto, Forecast.class);

            Optional<City> optionalCity = this.cityRepository.findById(dto.getCityId());
            forecast.setCity(optionalCity.get());

            this.forecastRepository.save(forecast);

            message = String.format("Successfully import forecast %s - %.2f%n",
                    forecast.getDayOfWeek(), forecast.getMaxTemperature());

        } else {
            message = String.format(INVALID_FORMAT, "Forecast");
        }

        return message;
    }

    @Override
    public String exportForecasts() {
        List<Forecast> forecasts = this.forecastRepository
                .findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(SUNDAY, 150000);

        StringBuilder sb = new StringBuilder();

        forecasts.forEach(f -> {
            sb.append(String.format("City: %s:%n" +
                    "-min temperature: %.2f%n" +
                    "--max temperature: %.2f%n" +
                    "---sunrise: %s%n" +
                    "----sunset: %s",
                    f.getCity().getCityName(),
                    f.getMinTemperature(),
                    f.getMaxTemperature(),
                    f.getSunrise().toString(),
                    f.getSunset().toString()
                    ));
            sb.append(System.lineSeparator());
        });

        return sb.toString().trim();
    }
}
