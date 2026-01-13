/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.domain.model.messstelle;

import de.muenchen.dave.geodataeai.domain.model.feature.FeaturePropertiesModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Fahrzeugklasse;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.ZaehldatenIntervall;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MessfaehigkeitModel implements FeaturePropertiesModel {

    private Integer mstId;

    private LocalDate gueltigAb;

    private LocalDate gueltigBis;

    private Fahrzeugklasse fahrzeugklasse;

    private ZaehldatenIntervall intervall;

}
