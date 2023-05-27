package com.example.football.models.dto.playersDTOs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPlayerRootDTO {

    @XmlElement(name = "player")
    private List<ImportPlayerDTO> players;

    public ImportPlayerRootDTO() {
        this.players = new ArrayList<>();
    }

    public List<ImportPlayerDTO> getPlayers() {
        return players;
    }
}
