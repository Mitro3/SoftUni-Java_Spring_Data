package com.example.football.service.impl;

import com.example.football.models.dto.teamDTOs.ImportTeamDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.football.constants.Paths.TEAMS_JSON_PATH;

//ToDo - Implement all methods
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final TownRepository townRepository;


    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return String.join("\n", Files.readAllLines(TEAMS_JSON_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        String json = this.readTeamsFileContent();

        ImportTeamDTO[] teamsDTOS = this.gson.fromJson(json, ImportTeamDTO[].class);

        return Arrays.stream(teamsDTOS)
                .map(this::importTeam)
                .collect(Collectors.joining("\n"));
    }

    private String importTeam(ImportTeamDTO dto) {
        Set<ConstraintViolation<ImportTeamDTO>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return "Invalid Team";
        }

        Optional<Team> optionalTeam = this.teamRepository.findByName(dto.getName());

        if (optionalTeam.isPresent()) {
            return "Invalid Team";
        }

        Team team = this.modelMapper.map(dto, Team.class);
        Optional<Town> town = this.townRepository.findByName(dto.getTownName());

        team.setTown(town.get());

        this.teamRepository.save(team);

        return "Successfully imported Team " + team;

    }
}
