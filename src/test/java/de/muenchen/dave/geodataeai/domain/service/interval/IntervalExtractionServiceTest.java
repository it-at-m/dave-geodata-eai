package de.muenchen.dave.geodataeai.domain.service.interval;

import de.muenchen.dave.geodataeai.domain.mapper.MesswerteResponseDomainMapperImpl;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.mobidam.eai.gen.api.MqMesswerteControllerApi;
import de.muenchen.mobidam.eai.gen.model.LoadMesswerteTimeRangeFzTypenParameterInner;
import de.muenchen.mobidam.eai.gen.model.LoadMesswerteTimeRangeTagestypenParameterInner;
import de.muenchen.mobidam.eai.gen.model.MessquerschnitteDto;
import de.muenchen.mobidam.eai.gen.model.MqMesswerteDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntervalExtractionServiceTest {

    private IntervalExtractionService intervalExtractionService;

    @Mock
    private MqMesswerteControllerApi mqMesswerteControllerApi;

    @BeforeEach
    void beforeEach() {
        intervalExtractionService = new IntervalExtractionService(
                mqMesswerteControllerApi,
                new MesswerteResponseDomainMapperImpl());
        Mockito.reset(mqMesswerteControllerApi);
    }

    @Test
    void getIntervalle() {
        final var format = " TEST DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS SUMME_KRAFTFAHRZEUGVERKEHR ANZAHL_PKW ANZAHL_PKWA ANZAHL_LKW ANZAHL_LKWA ANZAHL_KRAD ANZAHL_LFW ANZAHL_SATTEL_KFZ ANZAHL_BUS ANZAHL_NK_KFZ SUMME_ALLE_PKW SUMME_LASTZUG SUMME_GUETERVERKEHR SUMME_SCHWERVERKEHR ANZAHL_RAD TEST";

        final var intervalData1 = List.of("15", "2024-09-01 13:15:21", "2024-09-01 13:30:21", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
                "81", "82", "83", "84", "15");
        final var intervalData2 = List.of("15", "2024-09-01 14:15:21", "2024-09-01 14:30:21", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "15");
        final var intervalData3 = List.of("15", "2024-09-01 15:15:21", "2024-09-01 15:30:21", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                "51", "52", "53", "54", "15");

        final var messwerte1 = new MqMesswerteDto();
        messwerte1.setFormat(format);
        messwerte1.setTotalPages(3);
        messwerte1.setPageNumber(0);
        messwerte1.setPageSize(100000);
        messwerte1.setMessquerschnitte(new ArrayList<>());
        final var messquerschnitte1 = new MessquerschnitteDto();
        messquerschnitte1.setMqId(995L);
        messquerschnitte1.setIntervalle(List.of(intervalData1));
        messwerte1.getMessquerschnitte().add(messquerschnitte1);

        final var messwerte2 = new MqMesswerteDto();
        messwerte2.setFormat(format);
        messwerte2.setTotalPages(3);
        messwerte2.setPageNumber(1);
        messwerte2.setPageSize(100000);
        messwerte2.setMessquerschnitte(new ArrayList<>());
        final var messquerschnitte2 = new MessquerschnitteDto();
        messquerschnitte2.setMqId(996L);
        messquerschnitte2.setIntervalle(List.of(intervalData2));
        messwerte2.getMessquerschnitte().add(messquerschnitte2);

        final var messwerte3 = new MqMesswerteDto();
        messwerte3.setFormat(format);
        messwerte3.setTotalPages(3);
        messwerte3.setPageNumber(2);
        messwerte3.setPageSize(100000);
        messwerte3.setMessquerschnitte(new ArrayList<>());
        final var messquerschnitte3 = new MessquerschnitteDto();
        messquerschnitte3.setMqId(997L);
        messquerschnitte3.setIntervalle(List.of(intervalData3));
        messwerte3.getMessquerschnitte().add(messquerschnitte3);

        Mockito.when(mqMesswerteControllerApi.loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                0,
                100000)).thenReturn(Mono.just(messwerte1));

        Mockito.when(mqMesswerteControllerApi.loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                1,
                100000)).thenReturn(Mono.just(messwerte2));

        Mockito.when(mqMesswerteControllerApi.loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                2,
                100000)).thenReturn(Mono.just(messwerte3));

        final var result = intervalExtractionService.getIntervalle(
                List.of(1, 2, 3),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                LocalTime.of(0, 0),
                LocalTime.of(23, 59, 59),
                DaveTagesTyp.DTV_W5)
                .toList();

        var expectedIntervals = new ArrayList<IntervalModel>();
        var expected = new IntervalModel();
        expected.setMqId(995);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 13, 15, 21));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 13, 30, 21));
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(70));
        expected.setAnzahlLkw(BigDecimal.valueOf(73));
        expected.setAnzahlKrad(BigDecimal.valueOf(75));
        expected.setAnzahlLfw(BigDecimal.valueOf(76));
        expected.setAnzahlBus(BigDecimal.valueOf(78));
        expected.setSummeAllePkw(BigDecimal.valueOf(80));
        expected.setSummeLastzug(BigDecimal.valueOf(81));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(82));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(83));
        expected.setAnzahlRad(BigDecimal.valueOf(84));
        expectedIntervals.add(expected);

        expected = new IntervalModel();
        expected.setMqId(996);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 14, 15, 21));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 14, 30, 21));
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(20));
        expected.setAnzahlLkw(BigDecimal.valueOf(23));
        expected.setAnzahlKrad(BigDecimal.valueOf(25));
        expected.setAnzahlLfw(BigDecimal.valueOf(26));
        expected.setAnzahlBus(BigDecimal.valueOf(28));
        expected.setSummeAllePkw(BigDecimal.valueOf(30));
        expected.setSummeLastzug(BigDecimal.valueOf(31));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(32));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(33));
        expected.setAnzahlRad(BigDecimal.valueOf(34));
        expectedIntervals.add(expected);

        expected = new IntervalModel();
        expected.setMqId(997);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 15, 15, 21));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 15, 30, 21));
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(40));
        expected.setAnzahlLkw(BigDecimal.valueOf(43));
        expected.setAnzahlKrad(BigDecimal.valueOf(45));
        expected.setAnzahlLfw(BigDecimal.valueOf(46));
        expected.setAnzahlBus(BigDecimal.valueOf(48));
        expected.setSummeAllePkw(BigDecimal.valueOf(50));
        expected.setSummeLastzug(BigDecimal.valueOf(51));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(52));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(53));
        expected.setAnzahlRad(BigDecimal.valueOf(54));
        expectedIntervals.add(expected);

        Assertions.assertThat(result).isEqualTo(expectedIntervals);

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(1)).loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                0,
                100000);

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(1)).loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                1,
                100000);

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(1)).loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 2, 1),
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                2,
                100000);

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(3)).loadMesswerteFullRange(
                Mockito.anyList(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class),
                Mockito.anyList(),
                Mockito.anyList(),
                Mockito.any(Integer.class),
                Mockito.any(Integer.class));
    }

    @Test
    void getMesswerteLoadMesswerteFullRange() {
        final var messquerschnittIds = List.of(1, 2, 3);
        final var startDate = LocalDate.of(2020, 1, 1);
        final var endDate = LocalDate.of(2020, 2, 1);
        final var startTime = LocalTime.of(0, 0, 0);
        final var endTime = LocalTime.of(23, 59, 59);
        final var tagesTyp = DaveTagesTyp.DTV_W5;
        final var page = 2;
        final var size = 500;
        final var isFullRangeRequest = true;

        Mockito.when(mqMesswerteControllerApi.loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                startDate,
                endDate,
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                page,
                size)).thenReturn(Mono.just(new MqMesswerteDto()));

        final var result = intervalExtractionService.getMesswerte(
                messquerschnittIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp,
                page,
                size,
                isFullRangeRequest);

        Assertions.assertThat(result).isNotNull().isEqualTo(new MqMesswerteDto());

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(1)).loadMesswerteFullRange(
                List.of(1L, 2L, 3L),
                startDate,
                endDate,
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                page,
                size);

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(0)).loadMesswerteTimeRange(
                Mockito.anyList(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class),
                Mockito.any(String.class),
                Mockito.any(String.class),
                Mockito.anyList(),
                Mockito.anyList(),
                Mockito.any(Integer.class),
                Mockito.any(Integer.class));
    }

    @Test
    void getMesswerteLoadMesswerteTimeRange() {
        final var messquerschnittIds = List.of(1, 2, 3);
        final var startDate = LocalDate.of(2020, 1, 1);
        final var endDate = LocalDate.of(2020, 2, 1);
        final var startTime = LocalTime.of(1, 15, 0);
        final var endTime = LocalTime.of(15, 30, 0);
        final var tagesTyp = DaveTagesTyp.DTV_W5;
        final var page = 2;
        final var size = 500;
        final var isFullRangeRequest = false;

        Mockito.when(mqMesswerteControllerApi.loadMesswerteTimeRange(
                List.of(1L, 2L, 3L),
                startDate,
                endDate,
                "01:15:00",
                "15:30:00",
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                page,
                size)).thenReturn(Mono.just(new MqMesswerteDto()));

        final var result = intervalExtractionService.getMesswerte(
                messquerschnittIds,
                startDate,
                endDate,
                startTime,
                endTime,
                tagesTyp,
                page,
                size,
                isFullRangeRequest);

        Assertions.assertThat(result).isNotNull().isEqualTo(new MqMesswerteDto());

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(0)).loadMesswerteFullRange(
                Mockito.anyList(),
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class),
                Mockito.anyList(),
                Mockito.anyList(),
                Mockito.any(Integer.class),
                Mockito.any(Integer.class));

        Mockito.verify(mqMesswerteControllerApi, Mockito.times(1)).loadMesswerteTimeRange(
                List.of(1L, 2L, 3L),
                startDate,
                endDate,
                "01:15:00",
                "15:30:00",
                List.of(LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_DI_MI_DO, LoadMesswerteTimeRangeTagestypenParameterInner.WERKTAG_MO_FR),
                Arrays.asList(LoadMesswerteTimeRangeFzTypenParameterInner.values()),
                page,
                size);
    }

    @Test
    void isFullRangeRequest() {
        var startTime = LocalTime.of(0, 0);
        var endTime = LocalTime.of(23, 59, 59);
        var result = intervalExtractionService.isFullRangeRequest(startTime, endTime);
        Assertions.assertThat(result).isTrue();

        startTime = LocalTime.of(0, 0, 0, 1);
        endTime = LocalTime.of(23, 59, 59);
        result = intervalExtractionService.isFullRangeRequest(startTime, endTime);
        Assertions.assertThat(result).isFalse();

        startTime = LocalTime.of(0, 0);
        endTime = LocalTime.of(23, 58);
        result = intervalExtractionService.isFullRangeRequest(startTime, endTime);
        Assertions.assertThat(result).isFalse();

        startTime = LocalTime.of(0, 0);
        endTime = LocalTime.of(23, 30, 0);
        result = intervalExtractionService.isFullRangeRequest(startTime, endTime);
        Assertions.assertThat(result).isFalse();

        startTime = LocalTime.of(0, 1);
        endTime = LocalTime.of(23, 59, 59);
        result = intervalExtractionService.isFullRangeRequest(startTime, endTime);
        Assertions.assertThat(result).isFalse();

        startTime = LocalTime.of(10, 0);
        endTime = LocalTime.of(23, 59, 59);
        result = intervalExtractionService.isFullRangeRequest(startTime, endTime);
        Assertions.assertThat(result).isFalse();
    }
}
