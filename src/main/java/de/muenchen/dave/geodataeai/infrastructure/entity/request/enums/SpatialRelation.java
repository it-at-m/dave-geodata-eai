/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.entity.request.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SpatialRelation {
    INTERSECTS("esriSpatialRelIntersects");

    @Getter
    private final String spatialRel;
}
