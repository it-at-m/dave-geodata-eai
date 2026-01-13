package de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes(
    {
            @JsonSubTypes.Type(value = MultiPolygonGeometry.class, name = "MultiPolygon"),
            @JsonSubTypes.Type(value = PointGeometry.class, name = "Point"),
            @JsonSubTypes.Type(value = PolygonGeometry.class, name = "Polygon"),
    }
)
public abstract class Geometry {

    private String type;
}
