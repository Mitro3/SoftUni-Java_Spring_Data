package com.example.football.service.impl;

import com.example.football.models.dto.ImportStatDTO;
import com.example.football.models.dto.ImportStatRootDTO;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtilsImpl;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.football.models.entity.Constants.INVALID_FORMAT;

@Service
public class StatServiceImpl implements StatService {

    private static final String STAT_FILE_PATH = "src/main/resources/files/xml/stats.xml";

    private final StatRepository statRepository;

    private final XmlParser xmlParser;

    private final ValidationUtilsImpl validationUtils;

    private final ModelMapper modelMapper;

    @Autowired
    public StatServiceImpl(StatRepository statRepository, XmlParser xmlParser, ValidationUtilsImpl validationUtils, ModelMapper modelMapper) {
        this.statRepository = statRepository;
        this.xmlParser = xmlParser;
        this.validationUtils = validationUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STAT_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        ImportStatRootDTO stats = this.xmlParser
                .fromFile(Path.of(STAT_FILE_PATH).toFile(), ImportStatRootDTO.class);

        List<ImportStatDTO> importStats = stats.getStats();

        List<String> result = new ArrayList<>();

        for (ImportStatDTO stat : importStats) {
             result.add(importStat(stat));
        }

        return String.join("\n", result);
    }

    private String importStat(ImportStatDTO dto) {
        Optional<Stat> optionalStat = this.statRepository
                .findByShootingAndPassingAndEndurance(dto.getShooting(), dto.getPassing(), dto.getEndurance());

        if (optionalStat.isEmpty() && validationUtils.isValid(dto)) {
            Stat stat = this.modelMapper.map(dto, Stat.class);
            this.statRepository.save(stat);

            String message = String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                    stat.getPassing(), stat.getShooting(), stat.getEndurance());

            return message;

        } else {
            return String.format(INVALID_FORMAT, "Stat");
        }
    }
}
