package com.example.workshop.models.projects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "projects")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportProjectsDTO {

    @XmlElement(name = "project")
    List<ImportProjectDTO> projects;

    public ImportProjectsDTO () {
        this.projects = new ArrayList<>();
    }

    public List<ImportProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ImportProjectDTO> projects) {
        this.projects = projects;
    }
}
