package com.example.workshop.services;

import com.example.workshop.models.companies.Company;
import com.example.workshop.models.companies.ImportCompanyDTO;
import com.example.workshop.models.projects.ImportProjectsDTO;
import com.example.workshop.models.projects.Project;
import com.example.workshop.repositories.CompanyRepository;
import com.example.workshop.repositories.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper mapper;
    private final CompanyRepository companyRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ModelMapper modelMapper, ModelMapper mapper, CompanyRepository companyRepository) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.mapper = mapper;
        this.companyRepository = companyRepository;
    }

    public String getXMLContent() throws IOException {
        Path path = Path.of("src", "main", "resources","files", "xmls", "projects.xml");
        List<String> lines = Files.readAllLines(path);
        return String.join("\n", lines);
    }

    public void importProjects() throws IOException, JAXBException {
        String xmlContent = this.getXMLContent();
        ByteArrayInputStream stream = new ByteArrayInputStream(xmlContent.getBytes());

        JAXBContext jaxbContext = JAXBContext.newInstance(ImportProjectsDTO.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ImportProjectsDTO projects = (ImportProjectsDTO) unmarshaller.unmarshal(stream);

        List<Project> entities = projects.getProjects().stream()
                .map(dto -> this.mapper.map(dto, Project.class))
                .collect(Collectors.toList());

        this.projectRepository.saveAll(entities);
    }

}
