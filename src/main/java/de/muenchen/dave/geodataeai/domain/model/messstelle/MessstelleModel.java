package de.muenchen.dave.geodataeai.domain.model.messstelle;

import de.muenchen.dave.geodataeai.domain.model.feature.FeatureCollectionModel;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.feature.FeaturePropertiesModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MessstelleStatus;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MessstelleModel implements FeaturePropertiesModel {

    // Stammdaten
    private Integer mstId;

    private MessstelleStatus status;

    private LocalDate realisierungsdatum;

    private LocalDate abbaudatum;

    // Messstellen spezifische Attribute

    private String name;

    private Integer stadtbezirkNummer;

    private String bemerkung;

    private LocalDate datumLetztePlausibleMessung;

    private FeatureCollectionModel<FeatureModel<MessquerschnittModel>> messquerschnitte;

    private FeatureCollectionModel<FeatureModel<MessfaehigkeitModel>> messfaehigkeiten;
}
