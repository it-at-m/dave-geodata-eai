package de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PointGeometry extends Geometry {

    private List<BigDecimal> coordinates;
}
