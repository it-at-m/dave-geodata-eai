package de.muenchen.dave.geodataeai.domain.service.interval;

import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import de.muenchen.dave.geodataeai.domain.mapper.IntervalMapper;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalResponseModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalsForMqIdModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertRequestModel;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntervalService {

    private final IntervalAggregationService intervalAggregationService;

    private final IntervalAveragingService intervalAveragingService;

    private final IntervalMapper intervalMapper;

    /**
     * Die Methode extrahiert die Intervalle entsprechend der im Parameter gegebenen Informationen.
     * <p>
     * Es werden zwei Durchschnittsermittlungen ausgeführt:
     * <p>
     * - Bildet für jeden Intervall und jede Messquerschnitt-Id den Durchschnitt über die Messtage.
     * - Bildet für jeden Messquerschnitt den Durchschnitt der Messtage über die Tagessumme.
     * - Bildet für jeden Interval über jeden Messtag den Durchschnitt über die Summe selektierten
     * Messquerschnitte.
     * Messquerschnitte.
     *
     * @param request
     * @return die ermittelten Durschnitte für jeden Messtag je Messquerschnitt über alle
     *         Tagesintervalle und je Interval über die Messquerschnitte.
     * @throws FeatureRequestFailedException
     */
    public IntervalResponseModel getIntervals(final MesswertRequestModel request) throws FeatureRequestFailedException {
        try {
            final var aggregatedIntervalsByMqId = intervalAggregationService
                    .getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsGroupedByMqId(request);

            // Es sind alle Messquerschnitt der Messtelle enthalten. Wird z.B. für den Belastungsplan benötigt.
            final var allAggregatesIntervals = getAllIntervals(aggregatedIntervalsByMqId);

            // Bildet für jeden Intervall und jede Messquerschnitt-Id den Durchschnitt über die Messtage.
            final var meanForEachIntervalAndEachMessquerschnittOverMesstage = intervalAveragingService
                    .avaragingOfIntervalsAndMessquerschnittOverMesstage(allAggregatesIntervals)
                    .parallelStream()
                    .collect(Collectors.groupingByConcurrent(IntervalModel::getMqId))
                    .entrySet()
                    .parallelStream()
                    .map(intervalsByMqIdEntry -> {
                        final var intervals = intervalsByMqIdEntry.getValue()
                                .parallelStream()
                                .peek(interval -> interval.setMqId(intervalsByMqIdEntry.getKey()))
                                .sorted(Comparator.comparing(IntervalModel::getDatumUhrzeitVon))
                                .toList();
                        final var intervalResponseForMqId = new IntervalsForMqIdModel();
                        intervalResponseForMqId.setMqId(intervalsByMqIdEntry.getKey());
                        intervalResponseForMqId.setIntervals(intervals);
                        return intervalResponseForMqId;
                    })
                    .toList();

            // Bildet für jeden Messquerschnitt den Durchschnitt der Messtage über die Tagessumme.
            final var meanOfSummedUpDailyIntervalsForEachMessquerschnittOverMesstage = meanForEachIntervalAndEachMessquerschnittOverMesstage.stream()
                    .map(intervalsForMqIdModel -> {
                        final var deepCopyOfIntervalsForMqIdModel = intervalMapper.deepCopy(intervalsForMqIdModel);
                        final var summedUpInterval = deepCopyOfIntervalsForMqIdModel.getIntervals()
                                .parallelStream()
                                .reduce(new IntervalModel(), MesswertUtils::sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval);

                        deepCopyOfIntervalsForMqIdModel.setIntervals(List.of(summedUpInterval));
                        return deepCopyOfIntervalsForMqIdModel;
                    })
                    .toList();

            //Es sind nur die selektierten Messquerschnitte enthalten. Wird für alle Auswertungen ausser dem Belastungsplan benötigt.
            final var selectedAggregatesIntervals = meanForEachIntervalAndEachMessquerschnittOverMesstage.stream()
                    .filter(intervalsForMqIdModel -> request.getSelectedMessquerschnittIds().contains(intervalsForMqIdModel.getMqId()))
                    .flatMap(intervalsForMqIdModel -> intervalsForMqIdModel.getIntervals().stream())
                    .toList();

            // Bildet für jeden Interval über jeden Messtag den Durchschnitt über die Summe selektierten Messquerschnitte.
            final var deepCopyOfSelectedAggregatesIntervals = intervalMapper.deepCopy(selectedAggregatesIntervals);
            final var meanOfSummedUpMessquerschnitteForEachIntervalOverMesstage = intervalAveragingService
                    .averagingOfIntervalsOverMessquerschnittAndMesstag(deepCopyOfSelectedAggregatesIntervals)
                    .parallelStream()
                    .sorted(Comparator.comparing(IntervalModel::getDatumUhrzeitVon))
                    .toList();

            final var intervalResponse = new IntervalResponseModel();
            intervalResponse.setMeanOfSummedUpDailyIntervalsForEachMessquerschnittOverMesstage(meanOfSummedUpDailyIntervalsForEachMessquerschnittOverMesstage);
            intervalResponse.setMeanOfSummedUpMessquerschnitteForEachIntervalOverMesstage(meanOfSummedUpMessquerschnitteForEachIntervalOverMesstage);
            intervalResponse.setMeanForEachIntervalAndEachMessquerschnittOverMesstage(meanForEachIntervalAndEachMessquerschnittOverMesstage);
            intervalResponse.setIncludedMeasuringDays(0);

            if (!allAggregatesIntervals.isEmpty() && !selectedAggregatesIntervals.isEmpty()) {
                final var includedMeasuringDays = getIncludedMeasuringDays(request, allAggregatesIntervals.size(), aggregatedIntervalsByMqId.size());
                intervalResponse.setIncludedMeasuringDays(includedMeasuringDays);
            }

            return intervalResponse;
        } catch (Exception exception) {
            final var error = "Bei der Aggregation und Aufbereitung der Intervalle ist ein Fehler aufgetreten.";
            log.error(error, exception);
            throw new FeatureRequestFailedException(error, exception);
        }
    }

    protected int getIncludedMeasuringDays(
            final MesswertRequestModel request,
            final int sizeOfAllAggregatesIntervals,
            final int sizeOfAggregatedIntervalsByMqId) {
        final var intervalsPerHour = request.getIntervalInMinutes().getIntervalsPerDay() / 24;
        var endTimeHour = request.getEndTime().getHour();

        // Wenn es sich um einen ganzen Tag handelt, dann hat dieser 24 Stunden
        // LocalTime gibt dies aber mit LocalTime.MAX (23:59:59.999999999) an, daher muss der
        // Stunden-Wert um 1 erhöht werden.
        if (LocalTime.MAX.equals(request.getEndTime())) {
            endTimeHour++;
        }
        final var numberOfRelevantIntervals = (endTimeHour - request.getStartTime().getHour()) * intervalsPerHour;
        return sizeOfAllAggregatesIntervals / sizeOfAggregatedIntervalsByMqId / numberOfRelevantIntervals;
    }

    protected List<IntervalModel> getAllIntervals(final Map<Integer, List<IntervalModel>> intervalsByMqId) {
        return intervalsByMqId.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

}
