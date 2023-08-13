package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartImportDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartsRepository;
import softuni.exam.service.PartsService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.models.entity.Constants.*;

@Service
public class PartsServiceImpl implements PartsService {
    private final static String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";

    private final PartsRepository partsRepository;

    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtils validationUtils;

    @Autowired
    public PartsServiceImpl(PartsRepository partsRepository, ModelMapper modelMapper, Gson gson, ValidationUtils validationUtils) {
        this.partsRepository = partsRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtils = validationUtils;
    }

    @Override
    public boolean areImported() {
        return this.partsRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<PartImportDTO> partImportDTOS = Arrays.stream(this.gson.fromJson(readPartsFileContent(), PartImportDTO[].class))
                .collect(Collectors.toList());

        for (PartImportDTO part : partImportDTOS) {
            stringBuilder.append(System.lineSeparator());

            Optional<Part> optionalPart = this.partsRepository.findFirstByPartName(part.getPartName());

            if (optionalPart.isPresent() || !this.validationUtils.isValid(part)) {
                stringBuilder.append(String.format(INVALID_FORMAT, PART));
                continue;
            }

            this.partsRepository.save(this.modelMapper.map(part, Part.class));

            stringBuilder.append(String.format(SUCCESSFULL_FORMAT,
                    PART,
                    part.getPartName() + " -",
                    part.getPrice()));
        }

        return stringBuilder.toString().trim();
    }
}
