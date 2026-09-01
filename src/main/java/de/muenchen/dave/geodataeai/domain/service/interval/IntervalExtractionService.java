package de.muenchen.dave.geodataeai.domain.service.interval;

import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.mapper.MesswerteResponseDomainMapper;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.mobidam.eai.gen.api.MqMesswerteControllerApi;
import de.muenchen.mobidam.eai.gen.model.LoadMesswerteTimeRangeFzTypenParameterInner;
import de.muenchen.mobidam.eai.gen.model.MqMesswerteDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntervalExtractionService {

    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final MqMesswerteControllerApi mqMesswerteControllerApi;

    private final MesswerteResponseDomainMapper messwerteResponseDomainMapper;

    /**
     * Extrahiert die Intervalle auf Basis der gegebenen Methodenparameter aus Mobidam.
     *
     * @param messquerschnittIds für die Messquerschnitte
     * @param startDate als Startdatum der Periode
     * @param endDate als Enddatum der Periode
     * @param startTime als tagesbezogener Startzeitpunkt.
     * @param endTime als tagesbezogener Endzeitpunkt.
     * @param tagesTyp zur Auswahl der entsprechenden Intervalle.
     * @return die Intervalle
     */
    @LogExecutionTime
    public Stream<IntervalModel> getIntervalle(
            final List<Integer> messquerschnittIds,
            final LocalDate startDate,
            final LocalDate endDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final DaveTagesTyp tagesTyp) {
        final var isFullRangeRequest = isFullRangeRequest(startTime, endTime);

        final var intervals = new ArrayList<IntervalModel>();
        MqMesswerteDto messwerteResponse = new MqMesswerteDto();
        messwerteResponse.setTotalPages(2);
        for (int page = 0; page < ObjectUtils.getIfNull(messwerteResponse.getTotalPages(), 0); page++) {
            messwerteResponse = this.getMesswerte(
                    messquerschnittIds,
                    startDate,
                    endDate,
                    startTime,
                    endTime,
                    tagesTyp,
                    page,
                    100000,
                    isFullRangeRequest);
            final var extractedIntervals = messwerteResponseDomainMapper.messwerte2Intervals(messwerteResponse, tagesTyp);
            intervals.addAll(extractedIntervals);
        }
        return intervals.stream();
    }

    /**
     * Extrahiert die Messwerte aus Mobidam für die im Parameter angegebene Seite.
     *
     * @param messquerschnittIds für die Messquerschnitte.
     * @param startDate als Startdatum der Periode.
     * @param endDate als Enddatum der Periode.
     * @param startTime als tagesbezogener Startzeitpunkt.
     * @param endTime als tagesbezogener Endzeitpunkt.
     * @param tagesTyp zur Auswahl der entsprechenden Intervalle.
     * @param page als zero-based Seitennummer.
     * @param size als Seitengröße.
     * @param isFullRangeRequest zur Ausführung des entsprechenden Requests an Mobidam.
     * @return die Messwerte aus Mobidam.
     */
    protected MqMesswerteDto getMesswerte(
            final List<Integer> messquerschnittIds,
            final LocalDate startDate,
            final LocalDate endDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final DaveTagesTyp tagesTyp,
            final int page,
            final int size,
            final boolean isFullRangeRequest) {
        final var tagesTypen = tagesTyp.getMobidamMesswerteTagesTyp();
        final var fahrzeugtypen = Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values());
        try {
            final MqMesswerteDto messwerte;
            if (isFullRangeRequest) {
                messwerte = mqMesswerteControllerApi.loadMesswerteFullRange(
                        messquerschnittIds.stream().map(Long::valueOf).toList(),
                        startDate,
                        endDate,
                        tagesTypen,
                        fahrzeugtypen,
                        page,
                        size)
                        .block();
            } else {
                messwerte = mqMesswerteControllerApi.loadMesswerteTimeRange(
                        messquerschnittIds.stream().map(Long::valueOf).toList(),
                        startDate,
                        endDate,
                        startTime.format(ISO_DATE_TIME_FORMATTER),
                        endTime.format(ISO_DATE_TIME_FORMATTER),
                        tagesTypen,
                        fahrzeugtypen,
                        page,
                        size)
                        .block();
            }
            return messwerte;
        } catch (final Exception exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    /**
     * Die Methode prüft ob es sich um einen Full-Range-Request beginnend um 00:00:00 Uhr und endend um
     * 23:59:XX handelt.
     *
     * @param startTime welche den Zeitpunkt des Beginns der Range markiert.
     * @param endTime welche den Zeitpunkt des Endes der Range markiert.
     * @return true der Start- und Endzeitpunkt den ganzen Tag abdeckt, ansonsten false.
     */
    protected boolean isFullRangeRequest(
            final LocalTime startTime,
            final LocalTime endTime) {
        return LocalTime.MIN.equals(startTime)
                && endTime.getHour() == 23
                && endTime.getMinute() == 59;
    }

}
