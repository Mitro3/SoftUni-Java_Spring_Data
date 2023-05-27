package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportAgentDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;

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
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;

    private final Validator validator;
    private final Path AGENTS_JSON_PATH = Path.of("src/main/resources/files/json/agents.json");

    private final String INVALID_AGENT_MESSAGE = "Invalid agent";

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, TownRepository townRepository, Gson gson, ModelMapper modelMapper, Validator validator) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        List<String> strings = Files.readAllLines(AGENTS_JSON_PATH);
        return String.join("\n", strings);
    }

    @Override
    public String importAgents() throws IOException {
        String json = this.readAgentsFromFile();
        ImportAgentDTO[] importAgentDTOs = gson.fromJson(json, ImportAgentDTO[].class);

        return Arrays.stream(importAgentDTOs)
                .map(dto -> importAgent(dto))
                .collect(Collectors.joining("\n"));
    }

    private String importAgent(ImportAgentDTO dto) {
        Set<ConstraintViolation<ImportAgentDTO>> errors = this.validator.validate(dto);

        if (!errors.isEmpty()) {
            return INVALID_AGENT_MESSAGE;
        }

        Optional<Agent> optionalAgent = this.agentRepository.findByFirstName(dto.getFirstName());

        if (optionalAgent.isPresent()) {
            return INVALID_AGENT_MESSAGE;
        }


        Agent agent = this.modelMapper.map(dto, Agent.class);
        Optional<Town> optionalTown = this.townRepository.findByTownName(dto.getTown());

        if (optionalTown.isEmpty()) {
            return INVALID_AGENT_MESSAGE;
        }

        agent.setTown(optionalTown.get());
        agentRepository.save(agent);

        return "Successfully imported agent - " + agent.getFirstName() + agent.getLastName();
    }
}
