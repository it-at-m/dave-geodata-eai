/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.domain.model.feature;

import java.util.List;
import lombok.Data;

@Data
public class FeatureCollectionModel<FEATURE extends FeatureModel> {

    private String type = "FeatureCollection";

    private List<FEATURE> features;
}
