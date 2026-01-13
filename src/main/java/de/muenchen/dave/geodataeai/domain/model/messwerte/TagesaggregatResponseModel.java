package de.muenchen.dave.geodataeai.domain.model.messwerte;

import java.util.List;
import lombok.Data;

@Data
public class TagesaggregatResponseModel {

    private List<TagesaggregatModel> meanOfAggregatesForEachMqId;

    private TagesaggregatModel sumOverAllAggregatesOfAllMqId;
}
