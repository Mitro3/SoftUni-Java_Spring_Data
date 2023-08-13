package com.example.football.service.impl;

import com.example.football.models.dto.ImportTownDTO;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtilsImpl;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.football.models.entity.Constants.INVALID_FORMAT;

@Service
public class TownServiceImpl implements TownService {

    private static final String TOWN_FILE_PATH = "src/main/resources/files/json/towns.json";
    private final TownRepository townRepository;

    private final ValidationUtilsImpl validationUtils;

    private final ModelMapper modelMapper;

    private final Gson gson;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ValidationUtilsImpl validationUtils, ModelMapper modelMapper, Gson gson) {
        this.townRepository = townRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWN_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();
        ImportTownDTO[] importTownDTOS = this.gson.fromJson(json, ImportTownDTO[].class);

        List<String> result = new ArrayList<>();

        for (ImportTownDTO importTownDTO : importTownDTOS) {
            Optional<Town> optionalTown = this.townRepository.findByName(importTownDTO.getName());

            if (validationUtils.isValid(importTownDTO) && optionalTown.isEmpty()) {
                Town town = this.modelMapper.map(importTownDTO, Town.class);
                this.townRepository.save(town);

                String message = String.format("Successfully imported Town %s - %d", town.getName(), town.getPopulation());
                result.add(message);

            } else {
                result.add(String.format(INVALID_FORMAT, "Town"));
            }
        }

        return String.join("\n", result);
    }
}
