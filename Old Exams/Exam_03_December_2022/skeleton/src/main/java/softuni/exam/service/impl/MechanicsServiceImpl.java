package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicImportDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.service.MechanicsService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.exam.models.entity.Constants.*;
import static softuni.exam.models.entity.Constants.PART;

@Service
public class MechanicsServiceImpl implements MechanicsService {

    private final MechanicsRepository mechanicsRepository;
    private final Gson gson;

    private final ModelMapper modelMapper;

    private final ValidationUtils validationUtils;

    private final static String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";

    @Autowired
    public MechanicsServiceImpl(MechanicsRepository mechanicsRepository, Gson gson, ModelMapper modelMapper, ValidationUtils validationUtils) {
        this.mechanicsRepository = mechanicsRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return this.mechanicsRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANICS_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();

        List<MechanicImportDTO> mechanics = Arrays.stream(gson.fromJson(readMechanicsFromFile(), MechanicImportDTO[].class)).collect(Collectors.toList());

        for (MechanicImportDTO mechanic : mechanics) {
            stringBuilder.append(System.lineSeparator());

            if (this.mechanicsRepository.findFirstByEmail(mechanic.getEmail()).isPresent() ||
                    this.mechanicsRepository.findBFirstByFirstName(mechanic.getFirstName()).isPresent() ||
                    !this.validationUtils.isValid(mechanic)) {
                stringBuilder.append(String.format(INVALID_FORMAT, MECHANIC));
                continue;
            }

            this.mechanicsRepository.save(this.modelMapper.map(mechanic, Mechanic.class));

            stringBuilder.append(String.format(SUCCESSFULL_FORMAT,
                    MECHANIC,
                    mechanic.getFirstName(),
                    mechanic.getLastName()));
        }

        return stringBuilder.toString().trim();
    }
}
