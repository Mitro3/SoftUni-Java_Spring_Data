package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastDTO;
import softuni.exam.models.dto.ImportForecastRootDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static softuni.exam.models.entity.DayOfWeek.SUNDAY;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;

    private final Path FORECAST_XML_PATH = Path.of("src/main/resources/files/xml/forecasts.xml");

    private final String INVALID_FORECAST_MESSAGE = "Invalid forecast";

    private final ModelMapper modelMapper;

    private final Validator validator;

    private final Unmarshaller unmarshaller;
    private final CityRepository cityRepository;


    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, ModelMapper modelMapper, Validator validator, CityRepository cityRepository) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.validator = validator;
        this.cityRepository = cityRepository;

        JAXBContext context = JAXBContext.newInstance(ImportForecastRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();

        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(FORECAST_XML_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastRootDTO forecastRootDTO = (ImportForecastRootDTO) this.unmarshaller.unmarshal(new File(FORECAST_XML_PATH.toString()));

        return forecastRootDTO.getForecasts().stream()
                .map(this::importSingleForecast)
                .collect(Collectors.joining("\n"));
    }

    private String importSingleForecast(ImportForecastDTO dto) {
        Set<ConstraintViolation<ImportForecastDTO>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return INVALID_FORECAST_MESSAGE;
        }

        Optional<Forecast> optionalForecast = this.forecastRepository.findByCityIdAndDayOfWeek(dto.getCity(), dto.getDayOfWeek());

        if (optionalForecast.isPresent()) {
            return INVALID_FORECAST_MESSAGE;
        }

        Forecast forecast = this.modelMapper.map(dto, Forecast.class);
        Optional<City> optionalCity = this.cityRepository.findById(dto.getCity());
        forecast.setCity(optionalCity.get());

        this.forecastRepository.save(forecast);

        return String.format("Successfully import forecast %s - %.2f", forecast.getDayOfWeek(), forecast.getMaxTemperature());
    }

    @Override
    public String exportForecasts() {
        int minPopulation = 150000;
        List<Forecast> forecasts = this.forecastRepository.findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(SUNDAY, minPopulation);

//        List<Forecast> forecasts = this.forecastRepository.findAllByDayOfWeeKAndCityMinPopulation(SUNDAY, minPopulation);
        StringBuilder sb = new StringBuilder();
        forecasts.forEach(f -> {
            sb.append(String.format("City: %s:\n" +
                            "\t\t-min temperature: %.2f\n" +
                            "\t\t--max temperature: %.2f\n" +
                            "\t\t---sunrise: %s\n" +
                            "\t\t----sunset: %s\n",
                    f.getCity().getCityName(),
                    f.getMinTemperature(),
                    f.getMaxTemperature(),
                    f.getSunrise().toString(),
                    f.getSunset().toString()))
                    .append(System.lineSeparator());
        });

        return sb.toString();
    }
}
