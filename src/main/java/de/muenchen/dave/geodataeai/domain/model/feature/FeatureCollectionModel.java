package de.muenchen.dave.geodataeai.domain.model.feature;

import java.util.List;
import lombok.Data;

@Data
public class FeatureCollectionModel<FEATURE extends FeatureModel> {

    private String type = "FeatureCollection";

    private List<FEATURE> features;
}
