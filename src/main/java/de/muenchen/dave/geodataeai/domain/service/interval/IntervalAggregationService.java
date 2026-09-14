package de.muenchen.dave.geodataeai.domain.service.interval;

import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.SubintervalCollectorModel;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Die Klasse dient zur Aggregation der extrahierten Intervalle.
 * <p>
 * Eine Summierung und Durchschnittsbildung wird auf Basis dieser Klasse in anderen Klassen
 * vorgenommen.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IntervalAggregationService {

    private final IntervalExtractionService intervalExtractionService;

    /**
     * Die Methode aggregiert die Intervalle für alle Messquerschnitt-Ids entsprechend
     * {@link IntervalAggregationService#getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds}
     * und gruppiert die aggregierten Intervalle nach deren
     * Messquerschnitt-Ids.
     *
     * @param request zur Extraktion und Aggregation der Intervalle.
     * @return die aggregierten Intervalle gruppiert nach den Messquerschnitt-Ids.
     */
    @LogExecutionTime
    public Map<Integer, List<IntervalModel>> getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsGroupedByMqId(final MesswertRequestModel request) {
        return this.getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(request)
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(IntervalModel::getMqId));
    }

    /**
     * Die Methode aggregiert die Intervalle für alle Messquerschnitt-Ids welche auf Basis der gegebenen
     * Requestparameter aus Mobidam extrahiert werden.
     * <p>
     * Die Intervalaggregation wird auf Basis der im Requestparameter gegebenen Intervallgröße
     * {@link MesswertRequestModel#getIntervalInMinutes()} durchgeführt.
     * <p>
     * Die Intervalle werden wie folgt aggregiert:
     * <p>
     * Gewählte {@link IntervalSize#INTERVAL_60}:
     * Es werden das Intervall der Größe {@link IntervalSize#INTERVAL_60},
     * und vier Intervalle der Größe {@link IntervalSize#INTERVAL_15} pro MQ aggregiert.
     * <p>
     * Gewählte {@link IntervalSize#INTERVAL_30}:
     * Es werden zwei Intervalle der Größe {@link IntervalSize#INTERVAL_15} pro MQ aggregiert.
     * <p>
     * Gewählte {@link IntervalSize#INTERVAL_15}:
     * Es erfolgt keine Aggregation, das Intervall der Größe {@link IntervalSize#INTERVAL_15} bleibt pro
     * MQ erhalten.
     * <p>
     * Die oben beschrieben Aggregation bezieht sich auf die in Mobidam vorhandenen Intervalgrößen.
     *
     * @param request zur Extraktion und Aggregation der Intervalle.
     * @return die aggregierten Intervalle.
     */
    @LogExecutionTime
    public List<IntervalModel> getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(final MesswertRequestModel request) {
        final var allAggregatedIntervals = new CopyOnWriteArrayList<IntervalModel>();

        intervalExtractionService.fetchIntervalleFromMesswerteAsStream(
                request.getAllMessquerschnittIds(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getTagesTyp())
                .parallel()
                .filter(interval -> isIntervalAcceptedForAggregation(interval, request.getIntervalInMinutes()))
                .collect(Collectors.groupingByConcurrent(messwerteIntervallModel -> messwerteIntervallModel.getDatumUhrzeitVon().toLocalDate()))
                .forEach((messtag, intervalleMesstag) -> {
                    intervalleMesstag
                            .parallelStream()
                            .collect(Collectors.groupingByConcurrent(IntervalModel::getMqId))
                            .forEach((mqId, intervalleMqId) -> {

                                /**
                                 * Erstellung der {@link SubintervalCollectorModel}s um darin die Mobidam-Intervalle
                                 * für die angefragte {@link IntervalSize} sammeln und aggregieren zu können.
                                 */
                                final var subintervalCollectorsAccordingIntervalSize = createSubintervalCollectorsWithContainingIntervalWithoutData(
                                        mqId,
                                        messtag,
                                        request.getStartTime(),
                                        request.getEndTime(),
                                        request.getIntervalInMinutes());

                                /**
                                 * Jedem {@link SubintervalCollectorModel} werden die passenden Mobidam-Intervalle zugeordnet
                                 * um diese dann im {@link SubintervalCollectorModel} vereinigen zu können.
                                 */
                                final var aggregatedIntervals = subintervalCollectorsAccordingIntervalSize
                                        .parallelStream()
                                        // Hinzufügen der passenden Mobidam-Intervalle zu SubintervalCollector
                                        .peek(subintervalCollector -> intervalleMqId
                                                .forEach(subintervalCollector::collectIfSubintervalIsWithinVonBis))
                                        // Vereinigung der Mobidam-Intervalle zu einem aggregierten Intervall
                                        .map(SubintervalCollectorModel::unificationOfCollectedSubintervals)
                                        .toList();
                                allAggregatedIntervals.addAll(aggregatedIntervals);
                            });
                });

        return allAggregatedIntervals;
    }

    /**
     * Der Methode ermittelt die Intervallgröße.
     *
     * @param interval der Interval für die Ermittlung der Größe.
     * @return die Intervallgröße definiert dir Differenz in Minuten des Start- und Endzeitpunkts.
     */
    protected IntervalSize getIntervalSize(final IntervalModel interval) {
        var bisDateTime = interval.getDatumUhrzeitBis();
        if (interval.getDatumUhrzeitBis().getMinute() == 59) {
            // Relevant für den letzten Interval des Tages mit bis-Uhrzeit 23:59:59
            bisDateTime = bisDateTime.plusMinutes(1);
        } else if (interval.getDatumUhrzeitBis().isBefore(interval.getDatumUhrzeitVon())) {
            // Relevant für den letzten Interval des Tages mit bis-Uhrzeit 00:00:00 und selben Tagesdatum wie von-Datum
            final var bisDate = bisDateTime.toLocalDate().plusDays(1);
            final var bisTime = LocalTime.of(0, 0, 0);
            bisDateTime = LocalDateTime.of(bisDate, bisTime);
        }
        final var intervalInMinutes = Duration.between(interval.getDatumUhrzeitVon(), bisDateTime).toMinutes();
        return IntervalSize.getByMinutes(intervalInMinutes);
    }

    /**
     * Die Methode dient zur Prüfung ob ein Intevall für die Aggregierung erforderlich ist.
     *
     * @param interval zum Prüfen.
     * @param requestedIntervalSize die angeforderte Intervallgröße
     * @return false falls ein {@link IntervalSize#INTERVAL_15} angefordert ist und es sich um einen
     *         Interval der Größe {@link IntervalSize#INTERVAL_30} oder
     *         {@link IntervalSize#INTERVAL_60} handelt, oder falls ein ein
     *         {@link IntervalSize#INTERVAL_30} angefordert ist und es sich um einen Interval der
     *         Größe {@link IntervalSize#INTERVAL_60} handelt. Ansonsten wird true zurückgegeben.
     */
    protected boolean isIntervalAcceptedForAggregation(final IntervalModel interval, final IntervalSize requestedIntervalSize) {
        final var intervalSize = getIntervalSize(interval);
        if (requestedIntervalSize == IntervalSize.INTERVAL_15 && intervalSize != IntervalSize.INTERVAL_15) {
            return false;
        } else return requestedIntervalSize != IntervalSize.INTERVAL_30 || intervalSize != IntervalSize.INTERVAL_60;
    }

    /**
     * Die Methode wird zur Erstellung von {@link SubintervalCollectorModel}s benötigt,
     * die zur Aggregation der Mobidam-Intervalle dienen.
     *
     * @param mqId als ID des Messquerschnitts
     * @param messTag als Tagesdatum des Kollektors.
     * @param startTime als Anfangszeitpunkt des Kollektors
     * @param endTime als Endzeitpunkt des Kollektors
     * @return die Liste der Kollektoren mit jeweils einem Intervall ohne die Zählwerte ohne den
     *         TagesTyp und ohne Störungs- sowie Plausiinfo.
     */
    public List<SubintervalCollectorModel> createSubintervalCollectorsWithContainingIntervalWithoutData(
            final Integer mqId,
            final LocalDate messTag,
            final LocalTime startTime,
            final LocalTime endTime,
            final IntervalSize intervalSize) {
        final var subintervalCollectors = new ArrayList<SubintervalCollectorModel>();

        var intervalStartDateTime = LocalDateTime.of(
                messTag.getYear(),
                messTag.getMonth(),
                messTag.getDayOfMonth(),
                startTime.getHour(),
                startTime.getMinute(),
                startTime.getSecond());
        var endTimeReached = false;
        while (!endTimeReached) {
            final var interval = new IntervalModel();
            interval.setMqId(mqId);
            interval.setDatumUhrzeitVon(intervalStartDateTime);
            intervalStartDateTime = intervalStartDateTime.plusMinutes(intervalSize.getMinutes());
            interval.setDatumUhrzeitBis(intervalStartDateTime);
            final var endDateTimeToCheck = LocalDateTime.of(
                    messTag.getYear(),
                    messTag.getMonth(),
                    messTag.getDayOfMonth(),
                    endTime.getHour(),
                    endTime.getMinute(),
                    endTime.getSecond());
            if (intervalStartDateTime.isEqual(endDateTimeToCheck) || intervalStartDateTime.isAfter(endDateTimeToCheck)) {
                if (messTag.isBefore(interval.getDatumUhrzeitBis().toLocalDate())) {
                    interval.setDatumUhrzeitBis(endDateTimeToCheck);
                }
                endTimeReached = true;
            }
            subintervalCollectors.add(new SubintervalCollectorModel(interval));
        }
        return subintervalCollectors;
    }

}
