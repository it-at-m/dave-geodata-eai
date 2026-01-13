package de.muenchen.dave.geodataeai.domain.service.interval;

import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntervalAveragingService {

    private final IntervalSummationService intervalSummationService;

    /**
     * Führ jeden Zählwert in den im Parameter gegebenen Intervallen wird je Zählwert der Durchschnitt
     * über alle gegebenen Intervalle gebildet.
     *
     * @param intervals zur Durchschnittsbildung
     * @return ein Interval welcher den Durchschnitt eines jeden Zählwerts repräsentiert.
     */
    protected static IntervalModel avarageIntervalCountingValuesByNumberOfElements(final List<IntervalModel> intervals) {
        final var intervalSummedByMesstage = intervals
                .parallelStream()
                .reduce(
                        new IntervalModel(),
                        MesswertUtils::sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval);

        return divideIntervalCountingValuesByCounter(intervalSummedByMesstage, intervals.size());
    }

    protected static IntervalModel divideIntervalCountingValuesByCounter(final IntervalModel interval, long counter) {
        interval.setAnzahlLfw(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getAnzahlLfw(), counter));
        interval.setAnzahlKrad(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getAnzahlKrad(), counter));
        interval.setAnzahlLkw(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getAnzahlLkw(), counter));
        interval.setAnzahlBus(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getAnzahlBus(), counter));
        interval.setAnzahlRad(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getAnzahlRad(), counter));
        interval.setSummeAllePkw(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getSummeAllePkw(), counter));
        interval.setSummeLastzug(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getSummeLastzug(), counter));
        interval.setSummeGueterverkehr(
                MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getSummeGueterverkehr(), counter));
        interval.setSummeSchwerverkehr(
                MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getSummeSchwerverkehr(), counter));
        interval.setSummeKraftfahrzeugverkehr(
                MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(interval.getSummeKraftfahrzeugverkehr(), counter));
        return interval;
    }

    /**
     * Die Methode bildet für jeden Messquerschnitt den Durchschnitt über die Tagessumme eines Messtags.
     *
     * Die Methode bildet je Messquerschnitt die Summe über alle Intervalle eines Messtags.
     *
     * @param intervals für die Summenbildung
     * @return den Durchschnitt über die Tagessumme eines Messtags je Messquerschnitt.
     */
    @LogExecutionTime
    public List<IntervalModel> averagingOfSummedUpDailyIntervalsOverMesstageForEachMessquerschnitt(final List<IntervalModel> intervals) {

        final var summationOfIntervalsForEachMessquerschnittByMesstag = intervalSummationService.summationOfIntervalsForEachMessquerschnittByMesstag(intervals);
        return summationOfIntervalsForEachMessquerschnittByMesstag
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(IntervalModel::getMqId))
                .values()
                .parallelStream()
                .map(IntervalAveragingService::avarageIntervalCountingValuesByNumberOfElements)
                .toList();
    }

    /**
     * Die Methode bildet für jeden Messtag je Interval den Durchschnitt über die Messquerschnitte.
     *
     * @param intervals für die Summenbildung
     * @return den Durchschnitt über die Messquerschnitte für jeden Interval eines Messtags.
     */
    @LogExecutionTime
    public List<IntervalModel> averagingOfIntervalsOverMessquerschnittAndMesstag(final List<IntervalModel> intervals) {
        final var sumOfIntervalsForEachEachMesstagByMessquerschnitt = intervalSummationService
                .summationOfIntervalsForEachMesstagByMessquerschnitt(intervals);

        return sumOfIntervalsForEachEachMesstagByMessquerschnitt
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(UhrzeitInterval::new))
                .values()
                .parallelStream()
                .map(IntervalAveragingService::avarageIntervalCountingValuesByNumberOfElements)
                .toList();
    }

    /**
     * Die Methode bildet je Messquerschnitt und je Interval den Durchschnitt über die Messtage.
     *
     * @param intervals für die Summenbildung
     * @return den Durchschnitt über die Messtage für jeden Interval und jeden Messquerschnitt.
     */
    public List<IntervalModel> avaragingOfIntervalsAndMessquerschnittOverMesstage(final List<IntervalModel> intervals) {

        return intervals
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(IntervalModel::getMqId))
                .entrySet()
                .parallelStream()
                .flatMap((intervalsOfMq) -> intervalsOfMq.getValue()
                        .parallelStream()
                        .collect(Collectors.groupingByConcurrent(MesswertUtils::getStartTimeFromInterval))
                        .values()
                        .parallelStream()
                        .map(IntervalAveragingService::avarageIntervalCountingValuesByNumberOfElements))
                .toList();
    }

    @Data
    private static class UhrzeitInterval {

        private final LocalTime uhrzeitVon;

        private final LocalTime uhrzeitBis;

        public UhrzeitInterval(final IntervalModel interval) {
            this.uhrzeitVon = interval.getDatumUhrzeitVon().toLocalTime();
            this.uhrzeitBis = interval.getDatumUhrzeitBis().toLocalTime();
        }
    }
}
