package de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArcgisRings extends ArcgisGeometry {

    /**
     * Die erste Listendimension beinhaltet die linearen Ringe des Polygons. Die zweite Listendimension
     * beinhaltet die Punktkoordinaten im Standard EPSG:4326
     * (WGS84). Die dritte Listendimension stellt eine Punktkoordinate dar. Die durch die Liste
     * repräsentierte Punktkoordinate muss der Länge 2 entsprechen und
     * somit einen X und einen Y-Abschnitt besitzen.
     * <p>
     * Jeder Ring ist ein linearer Ring und wird als eine Reihe von Punkten dargestellt. Der erste Punkt
     * eines jeden Rings ist immer derselbe wie der letzte
     * Punkt. Jeder Punkt in einem Ring wird als Zahlenfeld dargestellt.
     * <p>
     * Polygone sollten topologisch einfach sein. Äußere Ringe sind im Uhrzeigersinn orientiert, während
     * Löcher gegen den Uhrzeigersinn orientiert sind. Ringe
     * können sich an einem Scheitelpunkt berühren oder sich selbst an einem Scheitelpunkt berühren,
     * aber es sollte keine anderen Schnittpunkte geben. Die von
     * den Diensten zurückgegebenen Polygone sind topologisch einfach.
     * <p>
     * Siehe: https://developers.arcgis.com/documentation/common-data-types/geometry-objects.htm#POLYGON
     */
    private List<List<List<BigDecimal>>> rings;
}
