package com.example.football.service.impl;

import com.example.football.models.dto.ImportTeamDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtilsImpl;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.football.models.entity.Constants.INVALID_FORMAT;

@Service
public class TeamServiceImpl implements TeamService {
    private static final String TEAM_FILE_PATH = "src/main/resources/files/json/teams.json";

    private final TeamRepository teamRepository;

    private final TownRepository townRepository;

    private final Gson gson;

    private final ValidationUtilsImpl validationUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository, Gson gson, ValidationUtilsImpl validationUtils, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAM_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        String json = readTeamsFileContent();
        ImportTeamDTO[] importTeamDTOS = this.gson.fromJson(json, ImportTeamDTO[].class);

        List<String> result = new ArrayList<>();

        return Arrays.stream(importTeamDTOS).map(this::importTeam)
                .collect(Collectors.joining("\n"));
    }

    private String importTeam(ImportTeamDTO dto) {
        Optional<Team> optionalTeam = this.teamRepository.findByName(dto.getName());

        if (optionalTeam.isEmpty() && validationUtils.isValid(dto)) {
            Team team = this.modelMapper.map(dto, Team.class);
            Optional<Town> town = this.townRepository.findByName(dto.getTownName());

            team.setTown(town.get());

            this.teamRepository.save(team);

            String message = String.format("Successfully imported Team %s - %d", team.getName(), team.getFanBase());

            return message;

        } else {
            return String.format(INVALID_FORMAT, "Team");
        }
    }
}
