package de.muenchen.dave.geodataeai.domain.model.messwerte;

import java.util.List;
import lombok.Data;

@Data
public class IntervalResponseModel {

    private List<IntervalsForMqIdModel> meanOfSummedUpDailyIntervalsForEachMessquerschnittOverMesstage;

    private List<IntervalModel> meanOfSummedUpMessquerschnitteForEachIntervalOverMesstage;

    private List<IntervalsForMqIdModel> meanForEachIntervalAndEachMessquerschnittOverMesstage;

    private Integer includedMeasuringDays;

}
