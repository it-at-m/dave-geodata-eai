package de.muenchen.dave.geodataeai.api.dto.messstelle;

import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Fahrzeugklasse;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MessstelleStatus;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Verkehrsart;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class MessstelleDto {

    // Stammdaten
    private String mstId;

    private MessstelleStatus status;

    private LocalDate realisierungsdatum;

    private LocalDate abbaudatum;

    // Messstellen spezifische Attribute

    private String name;

    private Integer stadtbezirkNummer;

    private String bemerkung;

    private LocalDate datumLetztePlausibleMessung;

    private Fahrzeugklasse fahrzeugklasse;

    private Verkehrsart detektierteVerkehrsart;

    private String hersteller;

    private Double longitude;

    private Double latitude;

    private List<MessquerschnittDto> messquerschnitte;

    private List<MessfaehigkeitDto> messfaehigkeiten;
}
