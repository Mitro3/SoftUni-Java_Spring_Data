package com.example.football.service.impl;

import com.example.football.models.dto.townDTOs.ImportTownDTO;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.football.constants.Paths.TOWNS_JSON_PATH;


//ToDo - Implement all methods
@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(TOWNS_JSON_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();
        ImportTownDTO[] importTownDTOS = this.gson.fromJson(json, ImportTownDTO[].class);

        List<String> result = new ArrayList<>();

        for (ImportTownDTO importTownDTO : importTownDTOS) {
            Set<ConstraintViolation<ImportTownDTO>> validationErrors = this.validator.validate(importTownDTO);

            if (validationErrors.isEmpty()) {
                Optional<Town> optionalTown = this.townRepository.findByName(importTownDTO.getName());

                if (optionalTown.isPresent()) {
                    result.add("Invalid Town");
                } else {
                    Town town = this.modelMapper.map(importTownDTO, Town.class);
                    this.townRepository.save(town);

                    String message = String.format("Successfully imported %s - %d", town.getName(), town.getPopulation());
                    result.add(message);
                }

            } else {
                result.add("Invalid Town");
            }
        }

        return String.join("\n", result);
    }
}
