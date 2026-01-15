package de.muenchen.dave.geodataeai.domain.mapper;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.mobidam.eai.gen.model.MessquerschnitteDto;
import de.muenchen.mobidam.eai.gen.model.MqMesswerteDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MesswerteResponseDomainMapperTest {

    private MesswerteResponseDomainMapper mapper = new MesswerteResponseDomainMapperImpl();

    @Test
    void messwerte2Intervals() {
        final var format = " TEST DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS SUMME_KRAFTFAHRZEUGVERKEHR ANZAHL_PKW ANZAHL_PKWA ANZAHL_LKW ANZAHL_LKWA ANZAHL_KRAD ANZAHL_LFW ANZAHL_SATTEL_KFZ ANZAHL_BUS ANZAHL_NK_KFZ SUMME_ALLE_PKW SUMME_LASTZUG SUMME_GUETERVERKEHR SUMME_SCHWERVERKEHR ANZAHL_RAD TEST";
        final var messwerte = new MqMesswerteDto();
        messwerte.setFormat(format);
        messwerte.setMessquerschnitte(new ArrayList<>());

        final var intervalData11 = List.of("15", "2024-09-01 13:15:21", "2024-09-01 13:30:21", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
                "81", "82", "83", "84", "15");
        final var intervalData12 = List.of("15", "2024-09-01 14:15:21", "2024-09-01 14:30:21", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "15");

        var messquerschnitte = new MessquerschnitteDto();
        messquerschnitte.setMqId(995L);
        messquerschnitte.setIntervalle(List.of(intervalData11, intervalData12));
        messwerte.getMessquerschnitte().add(messquerschnitte);

        final var intervalData21 = List.of("15", "2024-09-01 23:45:00", "2024-09-01 00:00:00", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                "51", "52", "53", "54", "15");

        messquerschnitte = new MessquerschnitteDto();
        messquerschnitte.setMqId(998L);
        messquerschnitte.setIntervalle(List.of(intervalData21));
        messwerte.getMessquerschnitte().add(messquerschnitte);

        final var result = mapper.messwerte2Intervals(messwerte, DaveTagesTyp.DTV_W5);

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
        expected.setMqId(995);
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
        expected.setMqId(998);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 23, 45, 0));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 23, 59, 59));
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

        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result).isEqualTo(expectedIntervals);

    }

    @Test
    void messwert2IntervalAlleAttribute() {
        final var format = " TEST DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS SUMME_KRAFTFAHRZEUGVERKEHR ANZAHL_PKW ANZAHL_PKWA ANZAHL_LKW ANZAHL_LKWA ANZAHL_KRAD ANZAHL_LFW ANZAHL_SATTEL_KFZ ANZAHL_BUS ANZAHL_NK_KFZ SUMME_ALLE_PKW SUMME_LASTZUG SUMME_GUETERVERKEHR SUMME_SCHWERVERKEHR ANZAHL_RAD TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "2024-09-01 13:15:21", "2024-09-01 13:30:21", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80",
                "81", "82", "83", "84", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 13, 15, 21));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 13, 30, 21));
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

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalEverySecondAttributeNullStartingByFirstValueAttribute() {
        final var format = " TEST DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS SUMME_KRAFTFAHRZEUGVERKEHR ANZAHL_PKW ANZAHL_PKWA ANZAHL_LKW ANZAHL_LKWA ANZAHL_KRAD ANZAHL_LFW ANZAHL_SATTEL_KFZ ANZAHL_BUS ANZAHL_NK_KFZ SUMME_ALLE_PKW SUMME_LASTZUG SUMME_GUETERVERKEHR SUMME_SCHWERVERKEHR ANZAHL_RAD TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "2024-09-01 13:15:21", "2024-09-01 13:30:21", "NULL", "71", "NULL", "73", "NULL", "75", "NULL", "77", "NULL",
                "79", "NULL",
                "81", "NULL", "83", "NULL", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 13, 15, 21));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 13, 30, 21));
        expected.setSummeKraftfahrzeugverkehr(null);
        expected.setAnzahlLkw(BigDecimal.valueOf(73));
        expected.setAnzahlKrad(BigDecimal.valueOf(75));
        expected.setAnzahlLfw(null);
        expected.setAnzahlBus(null);
        expected.setSummeAllePkw(null);
        expected.setSummeLastzug(BigDecimal.valueOf(81));
        expected.setSummeGueterverkehr(null);
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(83));
        expected.setAnzahlRad(null);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalEverySecondAttributeNullStartingBySecondValueAttribute() {
        final var format = " TEST DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS SUMME_KRAFTFAHRZEUGVERKEHR ANZAHL_PKW ANZAHL_PKWA ANZAHL_LKW ANZAHL_LKWA ANZAHL_KRAD ANZAHL_LFW ANZAHL_SATTEL_KFZ ANZAHL_BUS ANZAHL_NK_KFZ SUMME_ALLE_PKW SUMME_LASTZUG SUMME_GUETERVERKEHR SUMME_SCHWERVERKEHR ANZAHL_RAD TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "2024-09-01 13:15:21", "2024-09-01 13:30:21", "70", "NULL", "72", "NULL", "74", "NULL", "76", "NULL", "78",
                "NULL", "80",
                "NULL", "82", "NULL", "84", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 13, 15, 21));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 13, 30, 21));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(70));
        expected.setAnzahlLkw(null);
        expected.setAnzahlKrad(null);
        expected.setAnzahlLfw(BigDecimal.valueOf(76));
        expected.setAnzahlBus(BigDecimal.valueOf(78));
        expected.setSummeAllePkw(BigDecimal.valueOf(80));
        expected.setSummeLastzug(null);
        expected.setSummeGueterverkehr(BigDecimal.valueOf(82));
        expected.setSummeSchwerverkehr(null);
        expected.setAnzahlRad(BigDecimal.valueOf(84));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalDatumUhrzeitVon() {
        final var format = " TEST DATUM_UHRZEIT_VON TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "2024-09-01 13:15:21", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 9, 1, 13, 15, 21));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalDatumUhrzeitBis() {
        final var format = " TEST DATUM_UHRZEIT_BIS TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "2024-09-01 13:15:21", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 9, 1, 13, 15, 21));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalKfzVerkehr() {
        final var format = " TEST SUMME_KRAFTFAHRZEUGVERKEHR TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalPkw() {
        final var format = " TEST ANZAHL_PKW TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalPkwA() {
        final var format = " TEST ANZAHL_PKWA TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalLkw() {
        final var format = " TEST ANZAHL_LKW TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setAnzahlLkw(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalLkwA() {
        final var format = " TEST ANZAHL_LKWA TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalKrad() {
        final var format = " TEST ANZAHL_KRAD TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setAnzahlKrad(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalLfw() {
        final var format = " TEST ANZAHL_LFW TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setAnzahlLfw(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalSattelKfz() {
        final var format = " TEST ANZAHL_SATTEL_KFZ TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalBus() {
        final var format = " TEST ANZAHL_BUS TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setAnzahlBus(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalNkKfz() {
        final var format = " TEST ANZAHL_NK_KFZ TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalAllePkw() {
        final var format = " TEST SUMME_ALLE_PKW TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeAllePkw(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalLastzug() {
        final var format = " TEST SUMME_LASTZUG TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeLastzug(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalGueterverkehr() {
        final var format = " TEST SUMME_GUETERVERKEHR TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeGueterverkehr(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalSchwerverkehr() {
        final var format = " TEST SUMME_SCHWERVERKEHR TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void messwert2IntervalRad() {
        final var format = " TEST ANZAHL_RAD TEST";
        final var messwerteElementIndex = new MesswerteResponseDomainMapper.MesswerteElementIndex(format);
        final var intervalData = List.of("15", "75", "15");
        final var mqId = 99L;

        final var result = mapper.messwert2Interval(mqId, intervalData, messwerteElementIndex, DaveTagesTyp.DTV_W5);

        final var expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setAnzahlRad(BigDecimal.valueOf(75));

        Assertions.assertThat(result).isEqualTo(expected);
    }

}
