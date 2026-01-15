package de.muenchen.dave.geodataeai.domain.model.messwerte;

import java.util.List;
import lombok.Data;

@Data
public class IntervalsForMqIdModel {

    private Integer mqId;

    private List<IntervalModel> intervals;

}
