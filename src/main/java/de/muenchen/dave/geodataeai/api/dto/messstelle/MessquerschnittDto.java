package de.muenchen.dave.geodataeai.api.dto.messstelle;

import lombok.Data;

@Data
public class MessquerschnittDto {

    private String mqId;

    private String mstId;

    private String strassenname;

    // Kommt aus Feld Beschreibung
    private String lageMessquerschnitt;

    private String fahrtrichtung;

    private Integer anzahlFahrspuren;

    private Integer anzahlDetektoren;

    private Double longitude;

    private Double latitude;
}
