package de.muenchen.dave.geodataeai.api.dto.messwerte;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class TagesaggregatResponseDto {

    @Schema(description = "Durchschnitt aller Tagesaggregate je angefragten Messquerschnitt-Ids.")
    private List<TagesaggregatDto> meanOfAggregatesForEachMqId;

    @Schema(description = "Summe aller Tagesaggregate über alle angefragten Messquerschnitt-Ids.")
    private TagesaggregatDto sumOverAllAggregatesOfAllMqId;
}
