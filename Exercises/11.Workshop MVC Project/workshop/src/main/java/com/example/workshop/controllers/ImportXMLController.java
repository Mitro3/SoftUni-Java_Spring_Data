package com.example.workshop.controllers;

import com.example.workshop.services.CompanyService;
import com.example.workshop.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Controller
public class ImportXMLController {

    private final CompanyService companyService;
    private final ProjectService projectService;
    @Autowired
    public ImportXMLController(CompanyService companyService, ProjectService projectService) {
        this.companyService = companyService;
        this.projectService = projectService;
    }

    @GetMapping("/import/xml")
    public String index(Model model) {
        boolean companiesImported = this.companyService.areImported();

        boolean[] importStatuses = {companiesImported, false, false};

        model.addAttribute("areImported", importStatuses);

        return "xml/import-xml";
    }

    @GetMapping("import/companies")
    public String viewImportCompanies(Model model) throws IOException {
        String companiesXML = this.companyService.getXMLContent();
        model.addAttribute("companies", companiesXML);

        return "xml/import-companies";
    }

    @PostMapping("import/companies")
    public String importCompanies() throws IOException, JAXBException {
        this.companyService.importCompanies();

        return "redirect:/import/xml";
    }


    @GetMapping("import/projects")
    public String viewImportProjects(Model model) throws IOException {
        String projectXML = this.projectService.getXMLContent();
        model.addAttribute("projects", projectXML);

        return "xml/import-projects";
    }

    @PostMapping("import/projects")
    public String importProject() throws JAXBException, IOException {
        this.projectService.importProjects();

        return "redirect:/import/xml";
    }
}
