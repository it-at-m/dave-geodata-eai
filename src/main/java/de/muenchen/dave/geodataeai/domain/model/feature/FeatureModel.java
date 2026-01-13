package de.muenchen.dave.geodataeai.domain.model.feature;

import de.muenchen.dave.geodataeai.domain.model.geometry.GeometryModel;
import lombok.Data;

/**
 * @param <TYPE_WITH_ATTRIBUTES> repräsentiert die Klassen welche die Attribute des jeweiligen
 *            Feature-Response beinhalten.
 */
@Data
public class FeatureModel<TYPE_WITH_ATTRIBUTES extends FeaturePropertiesModel> {

    private String type = "Feature";

    private GeometryModel geometry;

    private TYPE_WITH_ATTRIBUTES properties;
}
