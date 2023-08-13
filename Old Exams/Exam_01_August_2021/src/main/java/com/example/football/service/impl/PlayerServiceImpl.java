package com.example.football.service.impl;

import com.example.football.models.dto.ImportPlayerDTO;
import com.example.football.models.dto.ImportPlayerRootDTO;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtilsImpl;
import com.example.football.util.XmlParser;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.football.models.entity.Constants.INVALID_FORMAT;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYER_FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;

    private final TownRepository townRepository;

    private final TeamRepository teamRepository;

    private final StatRepository statRepository;

    private final ValidationUtilsImpl validationUtils;

    private final ModelMapper modelMapper;

    private final XmlParser xmlParser;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository, TeamRepository teamRepository, StatRepository statRepository, ValidationUtilsImpl validationUtils, ModelMapper modelMapper, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;

        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYER_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        ImportPlayerRootDTO importPlayerRootDTO = this.xmlParser
                .fromFile(Path.of(PLAYER_FILE_PATH).toFile(), ImportPlayerRootDTO.class);

        List<ImportPlayerDTO> players = importPlayerRootDTO.getPlayers();

        List<String> result = new ArrayList<>();

        for (ImportPlayerDTO importPlayerDTO : players) {
            result.add(importPlayer(importPlayerDTO));
        }

        return String.join("\n", result);
    }

    @Override
    public String exportBestPlayers() {
        LocalDate before = LocalDate.of(2003, 1, 1);
        LocalDate after = LocalDate.of(1995, 1, 1);

        List<Player> players = this.playerRepository
                .findByBirthDateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(after, before);

        return players
                .stream()
                .map(Player::toString)
                .collect(Collectors.joining("\n"));
    }

    private String importPlayer(ImportPlayerDTO dto) {
        Optional<Player> optionalPlayer = this.playerRepository.findByEmail(dto.getEmail());
        Optional<Town> optionalTown = this.townRepository.findByName(dto.getTown().getName());
        Optional<Team> optionalTeam = this.teamRepository.findByName(dto.getTeam().getName());
        Optional<Stat> optionalStat = this.statRepository.findById(dto.getStat().getId());

        if (optionalPlayer.isEmpty() && validationUtils.isValid(dto)) {
            Player player = this.modelMapper.map(dto, Player.class);
            player.setTown(optionalTown.get());
            player.setTeam(optionalTeam.get());
            player.setStat(optionalStat.get());

            this.playerRepository.save(player);

            String message = String.format("Successfully imported Player %s %s - %s",
                    player.getFirstName(), player.getLastName(), player.getPosition().toString());

            return message;

        } else {
            return String.format(INVALID_FORMAT, "Player");
        }
    }
}
