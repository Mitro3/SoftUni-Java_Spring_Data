package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarImportDTO;
import softuni.exam.models.dto.CarWrapperDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarsRepository;
import softuni.exam.service.CarsService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static softuni.exam.models.entity.Constants.*;

@Service
public class CarsServiceImpl implements CarsService {
    private static String CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml";

    private final CarsRepository carsRepository;

    private final ModelMapper modelMapper;

    private final ValidationUtils validationUtils;

    private final XmlParser xmlParser;

    @Autowired
    public CarsServiceImpl(CarsRepository carsRepository, ModelMapper modelMapper, ValidationUtils validationUtils, XmlParser xmlParser) {
        this.carsRepository = carsRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.carsRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<CarImportDTO> cars = this.xmlParser.fromFile(Path.of(CARS_FILE_PATH).toFile(), CarWrapperDTO.class)
                .getCars();

        for (CarImportDTO car : cars) {
            stringBuilder.append(System.lineSeparator());

            if(this.carsRepository.findFirstByPlateNumber(car.getPlateNumber()).isPresent()
                    || !this.validationUtils.isValid(car)) {
                stringBuilder.append(String.format(INVALID_FORMAT, CAR));
                continue;
            }

            this.carsRepository.save(this.modelMapper.map(car, Car.class));

            stringBuilder.append(String.format(SUCCESSFULL_FORMAT,
                    CAR,
                    car.getCarMake() + " -",
                    car.getCarModel()));
        }

        return stringBuilder.toString().trim();
    }
}
