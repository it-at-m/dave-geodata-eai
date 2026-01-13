package de.muenchen.dave.geodataeai.api.dto.messwerte;

import java.util.List;
import lombok.Data;

@Data
public class IntervalsForMqIdDto {

    private Integer mqId;

    private List<IntervalDto> intervals;

}
