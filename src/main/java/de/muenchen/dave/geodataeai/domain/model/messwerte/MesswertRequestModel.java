package de.muenchen.dave.geodataeai.domain.model.messwerte;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class MesswertRequestModel {

    private List<Integer> allMessquerschnittIds;

    private List<Integer> selectedMessquerschnittIds;

    private IntervalSize intervalInMinutes;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private DaveTagesTyp tagesTyp;
}
