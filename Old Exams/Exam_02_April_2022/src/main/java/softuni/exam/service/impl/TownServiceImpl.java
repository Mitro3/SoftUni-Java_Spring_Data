package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTownDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final Path TOWNS_JSON_PATH = Path.of("src/main/resources/files/json/towns.json");
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    private final String INVALID_TOWN_MESSAGE = "Invalid town";

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, Gson gson, Validator validator) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        List<String> strings = Files.readAllLines(TOWNS_JSON_PATH);
        return String.join("\n", strings);
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();
        ImportTownDTO[] importTownDTOS = gson.fromJson(json, ImportTownDTO[].class);

        return Arrays.stream(importTownDTOS)
                .map(dto -> ImportTown(dto))
                .collect(Collectors.joining("\n"));
    }

    private String ImportTown(ImportTownDTO dto) {
        Set<ConstraintViolation<ImportTownDTO>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return INVALID_TOWN_MESSAGE;
        }

        Optional<Town> optionalTown = this.townRepository.findByTownName(dto.getTownName());

        if (optionalTown.isPresent()) {
            return INVALID_TOWN_MESSAGE;
        }

        Town town = this.modelMapper.map(dto, Town.class);
        this.townRepository.save(town);

        return String.format("Successfully imported town %s - %d", town.getTownName(), town.getPopulation());
    }
}
