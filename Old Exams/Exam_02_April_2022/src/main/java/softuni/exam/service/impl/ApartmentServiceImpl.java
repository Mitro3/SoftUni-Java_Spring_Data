package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportApartmentDTO;
import softuni.exam.models.dto.ImportApartmentRootDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.XmlParser;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;

    private final TownRepository townRepository;

    private final String APARTMENT_XML_PATH = "src/main/resources/files/xml/apartments.xml";

    private final XmlParser xmlParser;

    private final Validator validator;

    private final String INVALID_APARTMENT_MESSAGE = "Invalid apartment";

    private final ModelMapper modelMapper;


    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository, XmlParser xmlParser, Validator validator, ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
        this.xmlParser = xmlParser;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return String.join("\n", Files.readAllLines(Path.of(APARTMENT_XML_PATH)));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        ImportApartmentRootDTO importApartmentRootDTO = xmlParser.fromFile(APARTMENT_XML_PATH, ImportApartmentRootDTO.class);

        return importApartmentRootDTO.getApartments().stream()
                .map(this::importSingleApartment)
                .collect(Collectors.joining("\n"));
    }

    private String importSingleApartment(ImportApartmentDTO dto) {
        Set<ConstraintViolation<ImportApartmentDTO>> errors = validator.validate(dto);

        if (!errors.isEmpty()) {
            return INVALID_APARTMENT_MESSAGE;
        }

        Optional<Town> optionalTown = this.townRepository.findByTownName(dto.getTown());

        Optional<Apartment> optionalApartment = this.apartmentRepository.findApartmentByAreaAndTown(dto.getArea(), optionalTown.get());

        if (optionalApartment.isPresent()) {
            return INVALID_APARTMENT_MESSAGE;
        }

        Apartment apartment = this.modelMapper.map(dto, Apartment.class);
        apartment.setTown(optionalTown.get());

        this.apartmentRepository.save(apartment);

        return String.format("Successfully imported apartment %s - %.2f", apartment.getApartmentType(), apartment.getArea());
    }
}
