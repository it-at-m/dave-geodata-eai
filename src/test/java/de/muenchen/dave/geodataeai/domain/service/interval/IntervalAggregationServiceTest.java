package de.muenchen.dave.geodataeai.domain.service.interval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.TestData;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.SubintervalCollectorModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntervalAggregationServiceTest {

    @Mock
    private IntervalExtractionService intervalExtractionService;

    private IntervalAggregationService intervalAggregationService;

    @BeforeEach
    public void beforeEach() {
        this.intervalAggregationService = new IntervalAggregationService(intervalExtractionService);
        Mockito.reset(intervalExtractionService);
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_60;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 8, 9);
        final var startTime = LocalTime.of(9, 0, 0);
        final var endTime = LocalTime.of(11, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final var zaehlwerteCounter = new AtomicInteger(1);
        final List<IntervalModel> intervals15Minutes = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp)
                .peek(interval -> {
                    var zaehlwert = BigDecimal.valueOf(zaehlwerteCounter.getAndIncrement());
                    setZaehlwerteToInterval(interval, zaehlwert);
                })
                .toList();

        zaehlwerteCounter.set(1);
        final List<IntervalModel> intervals60Minutes = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_60,
                tagesTyp)
                .peek(interval -> {
                    var zaehlwert = BigDecimal.valueOf(zaehlwerteCounter.getAndIncrement());
                    setZaehlwerteToInterval(interval, zaehlwert);
                })
                .toList();

        final var allIntervalsToReturn = new ArrayList<IntervalModel>();
        allIntervalsToReturn.addAll(intervals15Minutes);
        allIntervalsToReturn.addAll(intervals60Minutes);
        Collections.shuffle(allIntervalsToReturn);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(allIntervalsToReturn.stream());

        final Map<Integer, List<IntervalModel>> result = intervalAggregationService
                .getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsGroupedByMqId(request);

        final var expected = new HashMap<Integer, List<IntervalModel>>();

        var interval1 = new IntervalModel();
        interval1.setMqId(97);
        interval1.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 9, 0, 0));
        interval1.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 1            + 2            + 3            + 4            + 1            = 11
        interval1.setAnzahlLfw(BigDecimal.valueOf(11));
        interval1.setAnzahlKrad(BigDecimal.valueOf(11));
        interval1.setAnzahlLkw(BigDecimal.valueOf(11));
        interval1.setAnzahlBus(BigDecimal.valueOf(11));
        interval1.setAnzahlRad(BigDecimal.valueOf(11));
        interval1.setSummeAllePkw(BigDecimal.valueOf(11));
        interval1.setSummeLastzug(BigDecimal.valueOf(11));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(11));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(11));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(11));

        var interval2 = new IntervalModel();
        interval2.setMqId(97);
        interval2.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        interval2.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 11, 0, 0));
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 5            + 6            + 7            + 8            + 2            = 28
        interval2.setAnzahlLfw(BigDecimal.valueOf(28));
        interval2.setAnzahlKrad(BigDecimal.valueOf(28));
        interval2.setAnzahlLkw(BigDecimal.valueOf(28));
        interval2.setAnzahlBus(BigDecimal.valueOf(28));
        interval2.setAnzahlRad(BigDecimal.valueOf(28));
        interval2.setSummeAllePkw(BigDecimal.valueOf(28));
        interval2.setSummeLastzug(BigDecimal.valueOf(28));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(28));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(28));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(28));

        expected.put(97, List.of(interval1, interval2));

        interval1 = new IntervalModel();
        interval1.setMqId(98);
        interval1.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 9, 0, 0));
        interval1.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 9            + 10           + 11           + 12           + 3            = 45
        interval1.setAnzahlLfw(BigDecimal.valueOf(45));
        interval1.setAnzahlKrad(BigDecimal.valueOf(45));
        interval1.setAnzahlLkw(BigDecimal.valueOf(45));
        interval1.setAnzahlBus(BigDecimal.valueOf(45));
        interval1.setAnzahlRad(BigDecimal.valueOf(45));
        interval1.setSummeAllePkw(BigDecimal.valueOf(45));
        interval1.setSummeLastzug(BigDecimal.valueOf(45));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(45));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(45));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(45));

        interval2 = new IntervalModel();
        interval2.setMqId(98);
        interval2.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        interval2.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 11, 0, 0));
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 13           + 14           + 15           + 16           + 4            = 62
        interval2.setAnzahlLfw(BigDecimal.valueOf(62));
        interval2.setAnzahlKrad(BigDecimal.valueOf(62));
        interval2.setAnzahlLkw(BigDecimal.valueOf(62));
        interval2.setAnzahlBus(BigDecimal.valueOf(62));
        interval2.setAnzahlRad(BigDecimal.valueOf(62));
        interval2.setSummeAllePkw(BigDecimal.valueOf(62));
        interval2.setSummeLastzug(BigDecimal.valueOf(62));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(62));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(62));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(62));

        expected.put(98, List.of(interval1, interval2));

        interval1 = new IntervalModel();
        interval1.setMqId(99);
        interval1.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 9, 0, 0));
        interval1.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 17           + 18           + 19           + 20           + 5            = 79
        interval1.setAnzahlLfw(BigDecimal.valueOf(79));
        interval1.setAnzahlKrad(BigDecimal.valueOf(79));
        interval1.setAnzahlLkw(BigDecimal.valueOf(79));
        interval1.setAnzahlBus(BigDecimal.valueOf(79));
        interval1.setAnzahlRad(BigDecimal.valueOf(79));
        interval1.setSummeAllePkw(BigDecimal.valueOf(79));
        interval1.setSummeLastzug(BigDecimal.valueOf(79));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(79));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(79));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(79));

        interval2 = new IntervalModel();
        interval2.setMqId(99);
        interval2.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 10, 0, 0));
        interval2.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 11, 0, 0));
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 21           + 22           + 23           + 24           + 6            = 96
        interval2.setAnzahlLfw(BigDecimal.valueOf(96));
        interval2.setAnzahlKrad(BigDecimal.valueOf(96));
        interval2.setAnzahlLkw(BigDecimal.valueOf(96));
        interval2.setAnzahlBus(BigDecimal.valueOf(96));
        interval2.setAnzahlRad(BigDecimal.valueOf(96));
        interval2.setSummeAllePkw(BigDecimal.valueOf(96));
        interval2.setSummeLastzug(BigDecimal.valueOf(96));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(96));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(96));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(96));

        expected.put(99, List.of(interval1, interval2));

        assertThat(result.size(), is(3));
        assertThat(result.get(97), containsInAnyOrder(expected.get(97).toArray()));
        assertThat(result.get(98), containsInAnyOrder(expected.get(98).toArray()));
        assertThat(result.get(99), containsInAnyOrder(expected.get(99).toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsAtEndOfDay() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_60;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 8, 9);
        final var startTime = LocalTime.of(22, 0, 0);
        final var endTime = LocalTime.of(23, 59, 59);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final var zaehlwerteCounter = new AtomicInteger(1);
        final List<IntervalModel> intervals15Minutes = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp)
                .peek(interval -> {
                    var zaehlwert = BigDecimal.valueOf(zaehlwerteCounter.getAndIncrement());
                    setZaehlwerteToInterval(interval, zaehlwert);
                })
                .toList();

        zaehlwerteCounter.set(1);
        final List<IntervalModel> intervals60Minutes = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_60,
                tagesTyp)
                .peek(interval -> {
                    var zaehlwert = BigDecimal.valueOf(zaehlwerteCounter.getAndIncrement());
                    setZaehlwerteToInterval(interval, zaehlwert);
                })
                .toList();

        final var allIntervalsToReturn = new ArrayList<IntervalModel>();
        allIntervalsToReturn.addAll(intervals15Minutes);
        allIntervalsToReturn.addAll(intervals60Minutes);
        Collections.shuffle(allIntervalsToReturn);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(allIntervalsToReturn.stream());

        final Map<Integer, List<IntervalModel>> result = intervalAggregationService
                .getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsGroupedByMqId(request);

        final var expected = new HashMap<Integer, List<IntervalModel>>();

        var interval1 = new IntervalModel();
        interval1.setMqId(97);
        interval1.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 22, 0, 0));
        interval1.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 23, 0, 0));
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 1            + 2            + 3            + 4            + 1            = 11
        interval1.setAnzahlLfw(BigDecimal.valueOf(11));
        interval1.setAnzahlKrad(BigDecimal.valueOf(11));
        interval1.setAnzahlLkw(BigDecimal.valueOf(11));
        interval1.setAnzahlBus(BigDecimal.valueOf(11));
        interval1.setAnzahlRad(BigDecimal.valueOf(11));
        interval1.setSummeAllePkw(BigDecimal.valueOf(11));
        interval1.setSummeLastzug(BigDecimal.valueOf(11));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(11));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(11));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(11));

        var interval2 = new IntervalModel();
        interval2.setMqId(97);
        interval2.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 23, 0, 0));
        interval2.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 23, 59, 59));
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 5            + 6            + 7            + 8            + 2            = 28
        interval2.setAnzahlLfw(BigDecimal.valueOf(28));
        interval2.setAnzahlKrad(BigDecimal.valueOf(28));
        interval2.setAnzahlLkw(BigDecimal.valueOf(28));
        interval2.setAnzahlBus(BigDecimal.valueOf(28));
        interval2.setAnzahlRad(BigDecimal.valueOf(28));
        interval2.setSummeAllePkw(BigDecimal.valueOf(28));
        interval2.setSummeLastzug(BigDecimal.valueOf(28));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(28));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(28));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(28));

        expected.put(97, List.of(interval1, interval2));

        interval1 = new IntervalModel();
        interval1.setMqId(98);
        interval1.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 22, 0, 0));
        interval1.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 23, 0, 0));
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 9            + 10           + 11           + 12           + 3            = 45
        interval1.setAnzahlLfw(BigDecimal.valueOf(45));
        interval1.setAnzahlKrad(BigDecimal.valueOf(45));
        interval1.setAnzahlLkw(BigDecimal.valueOf(45));
        interval1.setAnzahlBus(BigDecimal.valueOf(45));
        interval1.setAnzahlRad(BigDecimal.valueOf(45));
        interval1.setSummeAllePkw(BigDecimal.valueOf(45));
        interval1.setSummeLastzug(BigDecimal.valueOf(45));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(45));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(45));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(45));

        interval2 = new IntervalModel();
        interval2.setMqId(98);
        interval2.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 23, 0, 0));
        interval2.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 23, 59, 59));
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 13           + 14           + 15           + 16           + 4            = 62
        interval2.setAnzahlLfw(BigDecimal.valueOf(62));
        interval2.setAnzahlKrad(BigDecimal.valueOf(62));
        interval2.setAnzahlLkw(BigDecimal.valueOf(62));
        interval2.setAnzahlBus(BigDecimal.valueOf(62));
        interval2.setAnzahlRad(BigDecimal.valueOf(62));
        interval2.setSummeAllePkw(BigDecimal.valueOf(62));
        interval2.setSummeLastzug(BigDecimal.valueOf(62));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(62));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(62));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(62));

        expected.put(98, List.of(interval1, interval2));

        interval1 = new IntervalModel();
        interval1.setMqId(99);
        interval1.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 22, 0, 0));
        interval1.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 23, 0, 0));
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 17           + 18           + 19           + 20           + 5            = 79
        interval1.setAnzahlLfw(BigDecimal.valueOf(79));
        interval1.setAnzahlKrad(BigDecimal.valueOf(79));
        interval1.setAnzahlLkw(BigDecimal.valueOf(79));
        interval1.setAnzahlBus(BigDecimal.valueOf(79));
        interval1.setAnzahlRad(BigDecimal.valueOf(79));
        interval1.setSummeAllePkw(BigDecimal.valueOf(79));
        interval1.setSummeLastzug(BigDecimal.valueOf(79));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(79));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(79));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(79));

        interval2 = new IntervalModel();
        interval2.setMqId(99);
        interval2.setDatumUhrzeitVon(LocalDateTime.of(2024, 8, 9, 23, 0, 0));
        interval2.setDatumUhrzeitBis(LocalDateTime.of(2024, 8, 9, 23, 59, 59));
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        // Intervall 15 + Intervall 15 + Intervall 15 + Intervall 15 + Intervall 60
        // 21           + 22           + 23           + 24           + 6            = 96
        interval2.setAnzahlLfw(BigDecimal.valueOf(96));
        interval2.setAnzahlKrad(BigDecimal.valueOf(96));
        interval2.setAnzahlLkw(BigDecimal.valueOf(96));
        interval2.setAnzahlBus(BigDecimal.valueOf(96));
        interval2.setAnzahlRad(BigDecimal.valueOf(96));
        interval2.setSummeAllePkw(BigDecimal.valueOf(96));
        interval2.setSummeLastzug(BigDecimal.valueOf(96));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(96));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(96));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(96));

        expected.put(99, List.of(interval1, interval2));

        assertThat(result.size(), is(3));
        assertThat(result.get(97), containsInAnyOrder(expected.get(97).toArray()));
        assertThat(result.get(98), containsInAnyOrder(expected.get(98).toArray()));
        assertThat(result.get(99), containsInAnyOrder(expected.get(99).toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsGroupedByMqId() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_15;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 9, 13);
        final var startTime = LocalTime.of(10, 0, 0);
        final var endTime = LocalTime.of(15, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final Stream<IntervalModel> responseForIntervalService = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(responseForIntervalService);

        final Map<Integer, List<IntervalModel>> result = intervalAggregationService
                .getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIdsGroupedByMqId(request);

        final Map<Integer, List<IntervalModel>> expected = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp)
                .collect(Collectors.groupingBy(IntervalModel::getMqId));

        assertThat(result.keySet(), containsInAnyOrder(Set.of(97, 98, 99).toArray()));
        assertThat(result.get(97), containsInAnyOrder(expected.get(97).toArray()));
        assertThat(result.get(98), containsInAnyOrder(expected.get(98).toArray()));
        assertThat(result.get(99), containsInAnyOrder(expected.get(99).toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsize15MinutesResponseContains15MinuteIntervalsForAllMqIds() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_15;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 9, 13);
        final var startTime = LocalTime.of(10, 0, 0);
        final var endTime = LocalTime.of(15, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final var responseForIntervalService = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                intervalSize,
                tagesTyp);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(responseForIntervalService);

        final List<IntervalModel> result = intervalAggregationService.getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(request);

        final Stream<IntervalModel> expected = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                intervalSize,
                tagesTyp);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsize15MinutesResponseContains15And60MinuteIntervalsForAllMqIds() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_15;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 9, 13);
        final var startTime = LocalTime.of(10, 0, 0);
        final var endTime = LocalTime.of(15, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final Stream<IntervalModel> responseForIntervalService15Minute = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp);

        final Stream<IntervalModel> responseForIntervalService60Minute = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_60,
                tagesTyp);

        final var responseForIntervalService = Stream.concat(responseForIntervalService15Minute, responseForIntervalService60Minute);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(responseForIntervalService);

        final List<IntervalModel> result = intervalAggregationService.getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(request);

        final Stream<IntervalModel> expected = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsize30MinutesResponseContains30MinuteIntervalsForAllMqIds() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_30;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 9, 13);
        final var startTime = LocalTime.of(10, 0, 0);
        final var endTime = LocalTime.of(15, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final Stream<IntervalModel> responseForIntervalService = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(responseForIntervalService);

        final List<IntervalModel> result = intervalAggregationService.getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(request);

        final Stream<IntervalModel> expected = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_30,
                tagesTyp);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsize60MinutesResponseContains60MinuteIntervalsForAllMqIds() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_60;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 9, 13);
        final var startTime = LocalTime.of(10, 0, 0);
        final var endTime = LocalTime.of(15, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final Stream<IntervalModel> responseForIntervalService = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_60,
                tagesTyp);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(responseForIntervalService);

        final List<IntervalModel> result = intervalAggregationService.getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(request);

        final Stream<IntervalModel> expected = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_60,
                tagesTyp);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void getAggregatedIntervalsAccordingRequestedIntervalsize60MinutesResponseContains15And60MinuteIntervalsForAllMqIds() {
        final var mqIds = List.of(97, 98, 99);
        final var intervalSize = IntervalSize.INTERVAL_60;
        final var startDate = LocalDate.of(2024, 8, 9);
        final var endDate = LocalDate.of(2024, 9, 13);
        final var startTime = LocalTime.of(10, 0, 0);
        final var endTime = LocalTime.of(15, 0, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;

        final var request = new MesswertRequestModel();
        request.setAllMessquerschnittIds(mqIds);
        request.setIntervalInMinutes(intervalSize);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setTagesTyp(tagesTyp);

        final Stream<IntervalModel> responseForIntervalService60Minutes = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                intervalSize,
                tagesTyp);

        final Stream<IntervalModel> responseForIntervalService15Minutes = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_15,
                tagesTyp)
                .peek(interval -> {
                    interval.setAnzahlLkw(null);
                });

        final var responseForIntervalService = Stream.concat(responseForIntervalService60Minutes, responseForIntervalService15Minutes);

        Mockito.when(intervalExtractionService.getIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp)).thenReturn(responseForIntervalService);

        final List<IntervalModel> result = intervalAggregationService.getAggregatedIntervalsAccordingRequestedIntervalsizeForAllMqIds(request);

        final List<IntervalModel> expected = TestData.createMesswertIntervalle(
                mqIds,
                startDate,
                endDate,
                startTime,
                endTime,
                IntervalSize.INTERVAL_60,
                tagesTyp)
                .peek(interval -> {
                    interval.setAnzahlLfw(BigDecimal.valueOf(8));
                    interval.setAnzahlKrad(BigDecimal.valueOf(8));
                    interval.setAnzahlLkw(BigDecimal.valueOf(4));
                    interval.setAnzahlBus(BigDecimal.valueOf(8));
                    interval.setAnzahlRad(BigDecimal.valueOf(8));
                    interval.setSummeAllePkw(BigDecimal.valueOf(8));
                    interval.setSummeLastzug(BigDecimal.valueOf(8));
                    interval.setSummeGueterverkehr(BigDecimal.valueOf(8));
                    interval.setSummeSchwerverkehr(BigDecimal.valueOf(8));
                    interval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(8));
                })
                .toList();

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void getIntervalSize() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        var result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_15));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_15));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59, 999999999));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_15));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_30));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_30));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59, 999999999));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_30));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_60));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_60));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59, 999999999));
        result = intervalAggregationService.getIntervalSize(interval);
        assertThat(result, is(IntervalSize.INTERVAL_60));
    }

    @Test
    void isIntervalAcceptedWithAcceptedForAggregationSize15Minutes() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        var result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_15);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_15);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_15);
        assertThat(result, is(false));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_15);
        assertThat(result, is(false));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_15);
        assertThat(result, is(false));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_15);
        assertThat(result, is(false));
    }

    @Test
    void isIntervalAcceptedWithAcceptedForAggregationSize30Minutes() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        var result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_30);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_30);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_30);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_30);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_30);
        assertThat(result, is(false));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_30);
        assertThat(result, is(false));
    }

    @Test
    void isIntervalAcceptedWithAcceptedForAggregationSize60Minutes() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        var result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_60);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_60);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_60);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_60);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_60);
        assertThat(result, is(true));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = intervalAggregationService.isIntervalAcceptedForAggregation(interval, IntervalSize.INTERVAL_60);
        assertThat(result, is(true));
    }

    @Test
    void createSubintervalCollectorsWithContainingIntervalWithoutDataIsNotTagesrand() {
        List<SubintervalCollectorModel> result = intervalAggregationService.createSubintervalCollectorsWithContainingIntervalWithoutData(
                999,
                LocalDate.of(2024, 2, 2),
                LocalTime.of(10, 0, 0),
                LocalTime.of(14, 0, 0),
                IntervalSize.INTERVAL_60);

        var expected = new ArrayList<SubintervalCollectorModel>();
        var interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 12, 0, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 12, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 13, 0, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 13, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 14, 0, 0));
        expected.add(new SubintervalCollectorModel(interval));

        assertThat(result, is(expected));
    }

    @Test
    void createSubintervalCollectorsWithContainingIntervalWithoutDataIsTagesrand() {
        List<SubintervalCollectorModel> result = intervalAggregationService.createSubintervalCollectorsWithContainingIntervalWithoutData(
                999,
                LocalDate.of(2024, 2, 2),
                LocalTime.of(22, 0, 0),
                LocalTime.of(23, 59, 59),
                IntervalSize.INTERVAL_30);

        var expected = new ArrayList<SubintervalCollectorModel>();
        var interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 22, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 22, 30, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 22, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        expected.add(new SubintervalCollectorModel(interval));

        assertThat(result, is(expected));
    }

    @Test
    void createIntervalsFor15MinutesWithoutDataIsTagesrand() {
        List<SubintervalCollectorModel> result = intervalAggregationService.createSubintervalCollectorsWithContainingIntervalWithoutData(
                999,
                LocalDate.of(2024, 2, 2),
                LocalTime.of(23, 0, 0),
                LocalTime.of(23, 59, 59),
                IntervalSize.INTERVAL_15);

        var expected = new ArrayList<SubintervalCollectorModel>();
        var interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 15, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        expected.add(new SubintervalCollectorModel(interval));

        interval = new IntervalModel();
        interval.setMqId(999);
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        expected.add(new SubintervalCollectorModel(interval));

        assertThat(result, is(expected));
    }

    public IntervalModel setZaehlwerteToInterval(final IntervalModel interval, final BigDecimal zaehlwert) {
        interval.setAnzahlLfw(zaehlwert);
        interval.setAnzahlKrad(zaehlwert);
        interval.setAnzahlLkw(zaehlwert);
        interval.setAnzahlBus(zaehlwert);
        interval.setAnzahlRad(zaehlwert);
        interval.setSummeAllePkw(zaehlwert);
        interval.setSummeLastzug(zaehlwert);
        interval.setSummeGueterverkehr(zaehlwert);
        interval.setSummeSchwerverkehr(zaehlwert);
        interval.setSummeKraftfahrzeugverkehr(zaehlwert);
        return interval;
    }

}
