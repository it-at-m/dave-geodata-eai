package de.muenchen.dave.geodataeai.infrastructure.entity.response.feature;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messfaehigkeit;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messquerschnitt;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messstelle;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messwerte.Tagesaggregat;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    {
            @JsonSubTypes.Type(value = Messstelle.class),
            @JsonSubTypes.Type(value = Messquerschnitt.class),
            @JsonSubTypes.Type(value = Messfaehigkeit.class),
            @JsonSubTypes.Type(value = Tagesaggregat.class),
    }
)
public interface FeatureProperties {
}
