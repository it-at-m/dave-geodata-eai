package de.muenchen.dave.geodataeai;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.SubintervalCollectorModel;
import de.muenchen.dave.geodataeai.domain.service.interval.IntervalAggregationService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static Stream<IntervalModel> createMesswertIntervalle(final List<Integer> messquerschnittIds,
            final LocalDate startDate,
            final LocalDate endDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final IntervalSize intervalSize,
            final DaveTagesTyp tagesTyp) {
        final var intervals = new ArrayList<IntervalModel>();
        final BigDecimal countValue;
        if (intervalSize == IntervalSize.INTERVAL_15) {
            countValue = BigDecimal.valueOf(1);
        } else if (intervalSize == IntervalSize.INTERVAL_30) {
            countValue = BigDecimal.valueOf(2);
        } else {
            countValue = BigDecimal.valueOf(4);
        }
        for (var messtag = startDate; messtag.isBefore(endDate.plusDays(1)); messtag = messtag.plusDays(1)) {

            for (final var mqId : messquerschnittIds) {
                new IntervalAggregationService(null)
                        .createSubintervalCollectorsWithContainingIntervalWithoutData(mqId, messtag, startTime, endTime, intervalSize)
                        .stream()
                        .map(SubintervalCollectorModel::getInterval)
                        .forEach(interval -> {
                            interval.setTagesTyp(tagesTyp);
                            interval.setAnzahlLfw(countValue);
                            interval.setAnzahlKrad(countValue);
                            interval.setAnzahlLkw(countValue);
                            interval.setAnzahlBus(countValue);
                            interval.setAnzahlRad(countValue);
                            interval.setSummeAllePkw(countValue);
                            interval.setSummeLastzug(countValue);
                            interval.setSummeGueterverkehr(countValue);
                            interval.setSummeSchwerverkehr(countValue);
                            interval.setSummeKraftfahrzeugverkehr(countValue);
                            intervals.add(interval);
                        });
            }
        }
        return intervals.stream();
    }

}
