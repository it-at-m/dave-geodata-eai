package de.muenchen.dave.geodataeai.api.dto.messwerte;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class IntervalResponseDto {

    @Schema(description = "Für jeden Messquerschnitt den Durchschnitt der Messtage über die Tagessumme.")
    private List<IntervalsForMqIdDto> meanOfSummedUpDailyIntervalsForEachMessquerschnittOverMesstage;

    @Schema(description = "Für jeden Interval über jeden Messtag den Durchschnitt über die Summe selektierten Messquerschnitte.")
    private List<IntervalDto> meanOfSummedUpMessquerschnitteForEachIntervalOverMesstage;

    @Schema(description = "Für jeden Intervall und jede Messquerschnitt-Id den Durchschnitt über die Messtage.")
    private List<IntervalsForMqIdDto> meanForEachIntervalAndEachMessquerschnittOverMesstage;

    @Schema(description = "Anzahl der Messtage, die in die Auswertung eingeflossenen sind.")
    private Integer includedMeasuringDays;

}
