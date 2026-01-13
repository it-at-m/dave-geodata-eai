package de.muenchen.dave.geodataeai.domain.service.interval;

import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntervalSummationService {

    /**
     * Die Methode bildet je Messquerschnitt die Summe über alle Intervalle eines Messtags.
     *
     * @param intervals für die Summenbildung
     * @return die Summe über alle Intervalle eines Messtages für jeden Messquerschnitt.
     */
    @LogExecutionTime
    public List<IntervalModel> summationOfIntervalsForEachMessquerschnittByMesstag(final List<IntervalModel> intervals) {
        return intervals
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(IntervalModel::getMqId))
                .entrySet()
                .parallelStream()
                .flatMap((intervalsOfMqId) -> {
                    final var summedIntervalsOfMqIdOfMesstag = new CopyOnWriteArrayList<IntervalModel>();

                    intervalsOfMqId.getValue()
                            .parallelStream()
                            .collect(Collectors.groupingByConcurrent(MesswertUtils::getStartDateFromInterval))
                            .forEach((messtag, intervalsOfMqIdOfMesstag) -> {
                                final var summedInterval = intervalsOfMqIdOfMesstag
                                        .parallelStream()
                                        .reduce(new IntervalModel(),
                                                MesswertUtils::sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval);
                                summedIntervalsOfMqIdOfMesstag.add(summedInterval);
                            });

                    return summedIntervalsOfMqIdOfMesstag.stream();
                })
                .toList();
    }

    /**
     * Die Methode bildet für jeden Messtag je Interval die Summe über die Messquerschnitte.
     *
     * @param intervals für die Summenbildung
     * @return die Summe über die Messquerschnitte für jeden Interval eines Messtags.
     */
    @LogExecutionTime
    public List<IntervalModel> summationOfIntervalsForEachMesstagByMessquerschnitt(final List<IntervalModel> intervals) {
        return intervals
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(MesswertUtils::getStartDateFromInterval))
                .entrySet()
                .parallelStream()
                .flatMap((intervallsOfMesstag) -> {
                    final var summedIntervalsDatumUhrzeit = new CopyOnWriteArrayList<IntervalModel>();

                    intervallsOfMesstag.getValue()
                            .parallelStream()
                            .collect(Collectors.groupingByConcurrent(DatumUhrzeitInterval::new))
                            .forEach((datumUhrzeitInterval, intervalsOfMesstagOfDatumUhrzeit) -> {
                                final var summedInterval = intervalsOfMesstagOfDatumUhrzeit
                                        .parallelStream()
                                        .peek(interval -> interval.setMqId(null))
                                        .reduce(new IntervalModel(), MesswertUtils::sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval);
                                summedIntervalsDatumUhrzeit.add(summedInterval);
                            });

                    return summedIntervalsDatumUhrzeit.stream();
                })
                .toList();
    }

    @Data
    private static class DatumUhrzeitInterval {

        private final LocalDateTime datumUhrzeitVon;

        private final LocalDateTime datumUhrzeitBis;

        public DatumUhrzeitInterval(final IntervalModel interval) {
            this.datumUhrzeitVon = interval.getDatumUhrzeitVon();
            this.datumUhrzeitBis = interval.getDatumUhrzeitBis();
        }
    }

}
