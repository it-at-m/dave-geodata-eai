/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.infrastructure.entity.request.enums;

import de.muenchen.dave.geodataeai.infrastructure.exception.GeometryNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

@AllArgsConstructor
@Slf4j
public enum GeometryType {
    POLYGON("esriGeometryPolygon", Polygon.class),

    POINT("esriGeometryPoint", Point.class);

    @Getter
    private final String geometryTypeDescription;

    @Getter
    private final Class<? extends Geometry> geometryClass;

    public static GeometryType getGeometryTypeForGeometry(final Geometry geometry) throws GeometryNotFoundException {
        return EnumUtils
                .getEnumList(GeometryType.class)
                .stream()
                .filter(geometryType -> geometryType.geometryClass.equals(geometry.getClass()))
                .findFirst()
                .orElseThrow(() -> new GeometryNotFoundException("Der GeometryTyp existiert nicht."));
    }
}
