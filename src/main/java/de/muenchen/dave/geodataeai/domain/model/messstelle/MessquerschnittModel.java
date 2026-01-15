package de.muenchen.dave.geodataeai.domain.model.messstelle;

import de.muenchen.dave.geodataeai.domain.model.feature.FeaturePropertiesModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Verkehrsart;
import lombok.Data;

@Data
public class MessquerschnittModel implements FeaturePropertiesModel {

    private Integer mqId;

    private Integer mstId;

    private String strassenname;

    // Lage Messquerschnitt
    private String beschreibung;

    private String fahrtrichtung;

    private Integer anzahlFahrspuren;

    private Verkehrsart detektierteVerkehrsart;

    private String hersteller;

    private Integer anzahlDetektoren;
}
