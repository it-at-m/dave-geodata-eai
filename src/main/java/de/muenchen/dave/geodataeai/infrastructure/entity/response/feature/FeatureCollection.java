/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.entity.response.feature;

import java.util.List;
import lombok.Data;

@Data
public class FeatureCollection<FEATURE extends Feature> {

    private String type = "FeatureCollection";

    private List<FEATURE> features;
}
