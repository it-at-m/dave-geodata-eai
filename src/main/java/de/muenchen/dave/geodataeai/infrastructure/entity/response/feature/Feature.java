/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.entity.response.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry.Geometry;
import lombok.Data;

/**
 * @param <TYPE_WITH_ATTRIBUTES> repräsentiert die Klassen welche die Attribute des jeweiligen
 *            Feature-Response beinhalten.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature<TYPE_WITH_ATTRIBUTES extends FeatureProperties> {

    private String type = "Feature";

    private Geometry geometry;

    private TYPE_WITH_ATTRIBUTES properties;
}
