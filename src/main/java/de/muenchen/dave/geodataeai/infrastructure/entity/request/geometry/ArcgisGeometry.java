package de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
        value = {
                @JsonSubTypes.Type(ArcgisPoint.class),
                @JsonSubTypes.Type(ArcgisRings.class)
        }
)
public abstract class ArcgisGeometry {
}
