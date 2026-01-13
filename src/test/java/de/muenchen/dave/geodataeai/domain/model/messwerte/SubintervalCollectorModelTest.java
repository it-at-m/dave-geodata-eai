package de.muenchen.dave.geodataeai.domain.model.messwerte;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SubintervalCollectorModelTest {

    @Test
    void collectIfSubintervalIsWithinVonBisAndSubintervalIsNotTagesrandlage() {
        final var subintervalCollector = createSubintervalCollectorModel(
                LocalDateTime.of(2024, 2, 2, 10, 0, 0),
                LocalDateTime.of(2024, 2, 2, 11, 0, 0));

        var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 0, 0, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 9, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 9, 45, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 9, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 45, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 15, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 11, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 30, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

    }

    @Test
    void collectIfSubintervalIsWithinVonBisAndSubintervalIsTagesrandlage() {
        final var subintervalCollector = createSubintervalCollectorModel(
                LocalDateTime.of(2024, 2, 2, 23, 0, 0),
                LocalDateTime.of(2024, 2, 2, 23, 59, 59));

        var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 22, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 22, 45, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 22, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 15, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(1));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 0, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 0, 15, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 0, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 0, 30, 0));
        subintervalCollector.getSubIntervals().clear();
        subintervalCollector.collectIfSubintervalIsWithinVonBis(interval);
        assertThat(subintervalCollector.getSubIntervals().size(), is(0));
    }

    @Test
    void isSubintervalWithinVonBisIsNotTagesrandlage() {
        final var subintervalCollector = createSubintervalCollectorModel(
                LocalDateTime.of(2024, 2, 2, 10, 0, 0),
                LocalDateTime.of(2024, 2, 2, 11, 0, 0));

        var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 9, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 9, 45, 0));
        var result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 9, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 45, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 15, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 11, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 30, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));
    }

    @Test
    void isSubintervalWithinVonBisIsTagesrandlage() {
        final var subintervalCollector = createSubintervalCollectorModel(
                LocalDateTime.of(2024, 2, 2, 23, 0, 0),
                LocalDateTime.of(2024, 2, 2, 23, 59, 59));

        var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 22, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 22, 45, 0));
        var result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 22, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 15, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 30, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 23, 59, 59));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(true));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 0, 0, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 0, 15, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));

        interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 0, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 0, 30, 0));
        result = subintervalCollector.isSubintervalWithinVonBis(interval);
        assertThat(result, is(false));
    }

    @Test
    void unificationOfCollectedSubintervals() {
        final var mainInterval = new IntervalModel();
        mainInterval.setMqId(999);
        mainInterval.setTagesTyp(null);
        mainInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 11, 0, 0));
        mainInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 12, 0, 0));
        final var subintervalCollector = new SubintervalCollectorModel(mainInterval);

        final var interval1 = new IntervalModel();
        interval1.setTagesTyp(DaveTagesTyp.DTV_W5);
        interval1.setAnzahlLfw(BigDecimal.valueOf(3));
        interval1.setAnzahlKrad(BigDecimal.valueOf(4));
        interval1.setAnzahlLkw(BigDecimal.valueOf(5));
        interval1.setAnzahlBus(BigDecimal.valueOf(8));
        interval1.setAnzahlRad(BigDecimal.valueOf(10));
        interval1.setSummeAllePkw(BigDecimal.valueOf(11));
        interval1.setSummeLastzug(BigDecimal.valueOf(12));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval2 = new IntervalModel();
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        interval2.setAnzahlLfw(BigDecimal.valueOf(3));
        interval2.setAnzahlKrad(BigDecimal.valueOf(4));
        interval2.setAnzahlLkw(BigDecimal.valueOf(5));
        interval2.setAnzahlBus(BigDecimal.valueOf(8));
        interval2.setAnzahlRad(BigDecimal.valueOf(10));
        interval2.setSummeAllePkw(BigDecimal.valueOf(11));
        interval2.setSummeLastzug(BigDecimal.valueOf(12));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval3 = new IntervalModel();
        interval3.setTagesTyp(DaveTagesTyp.DTV_W5);
        interval3.setAnzahlLfw(BigDecimal.valueOf(3));
        interval3.setAnzahlKrad(BigDecimal.valueOf(4));
        interval3.setAnzahlLkw(BigDecimal.valueOf(5));
        interval3.setAnzahlBus(BigDecimal.valueOf(8));
        interval3.setAnzahlRad(BigDecimal.valueOf(10));
        interval3.setSummeAllePkw(BigDecimal.valueOf(11));
        interval3.setSummeLastzug(BigDecimal.valueOf(12));
        interval3.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval3.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval3.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval4 = new IntervalModel();
        interval4.setTagesTyp(DaveTagesTyp.DTV_W5);
        interval4.setAnzahlLfw(BigDecimal.valueOf(3));
        interval4.setAnzahlKrad(BigDecimal.valueOf(4));
        interval4.setAnzahlLkw(BigDecimal.valueOf(5));
        interval4.setAnzahlBus(BigDecimal.valueOf(8));
        interval4.setAnzahlRad(BigDecimal.valueOf(10));
        interval4.setSummeAllePkw(BigDecimal.valueOf(11));
        interval4.setSummeLastzug(BigDecimal.valueOf(12));
        interval4.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval4.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval4.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        subintervalCollector.getSubIntervals().addAll(List.of(interval1, interval2, interval3, interval4));

        final var result = subintervalCollector.unificationOfCollectedSubintervals();
        final var expected = new IntervalModel();
        expected.setMqId(999);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 11, 0, 0));
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 12, 0, 0));
        expected.setAnzahlLfw(BigDecimal.valueOf(12));
        expected.setAnzahlKrad(BigDecimal.valueOf(16));
        expected.setAnzahlLkw(BigDecimal.valueOf(20));
        expected.setAnzahlBus(BigDecimal.valueOf(32));
        expected.setAnzahlRad(BigDecimal.valueOf(40));
        expected.setSummeAllePkw(BigDecimal.valueOf(44));
        expected.setSummeLastzug(BigDecimal.valueOf(48));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(52));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(56));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(60));

        assertThat(result, is(expected));
    }

    @Test
    void unificationOfCollectedSubintervalsWithInterval() {
        final var mainInterval = new IntervalModel();
        mainInterval.setMqId(999);
        mainInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        mainInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 11, 0, 0));
        mainInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 12, 0, 0));
        mainInterval.setAnzahlLfw(BigDecimal.valueOf(300));
        mainInterval.setAnzahlKrad(BigDecimal.valueOf(400));
        mainInterval.setAnzahlLkw(BigDecimal.valueOf(500));
        mainInterval.setAnzahlBus(BigDecimal.valueOf(800));
        mainInterval.setAnzahlRad(BigDecimal.valueOf(1000));
        mainInterval.setSummeAllePkw(BigDecimal.valueOf(1100));
        mainInterval.setSummeLastzug(BigDecimal.valueOf(1200));
        mainInterval.setSummeGueterverkehr(BigDecimal.valueOf(1300));
        mainInterval.setSummeSchwerverkehr(BigDecimal.valueOf(1400));
        mainInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(1500));
        final var subintervalCollector = new SubintervalCollectorModel(mainInterval);

        final var interval1 = new IntervalModel();
        interval1.setAnzahlLfw(BigDecimal.valueOf(3));
        interval1.setAnzahlKrad(BigDecimal.valueOf(4));
        interval1.setAnzahlLkw(BigDecimal.valueOf(5));
        interval1.setAnzahlBus(BigDecimal.valueOf(8));
        interval1.setAnzahlRad(BigDecimal.valueOf(10));
        interval1.setSummeAllePkw(BigDecimal.valueOf(11));
        interval1.setSummeLastzug(BigDecimal.valueOf(12));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval2 = new IntervalModel();
        interval2.setAnzahlLfw(BigDecimal.valueOf(3));
        interval2.setAnzahlKrad(BigDecimal.valueOf(4));
        interval2.setAnzahlLkw(BigDecimal.valueOf(5));
        interval2.setAnzahlBus(BigDecimal.valueOf(8));
        interval2.setAnzahlRad(BigDecimal.valueOf(10));
        interval2.setSummeAllePkw(BigDecimal.valueOf(11));
        interval2.setSummeLastzug(BigDecimal.valueOf(12));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval3 = new IntervalModel();
        interval3.setAnzahlLfw(BigDecimal.valueOf(3));
        interval3.setAnzahlKrad(BigDecimal.valueOf(4));
        interval3.setAnzahlLkw(BigDecimal.valueOf(5));
        interval3.setAnzahlBus(BigDecimal.valueOf(8));
        interval3.setAnzahlRad(BigDecimal.valueOf(10));
        interval3.setSummeAllePkw(BigDecimal.valueOf(11));
        interval3.setSummeLastzug(BigDecimal.valueOf(12));
        interval3.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval3.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval3.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval4 = new IntervalModel();
        interval4.setAnzahlLfw(BigDecimal.valueOf(3));
        interval4.setAnzahlKrad(BigDecimal.valueOf(4));
        interval4.setAnzahlLkw(BigDecimal.valueOf(5));
        interval4.setAnzahlBus(BigDecimal.valueOf(8));
        interval4.setAnzahlRad(BigDecimal.valueOf(10));
        interval4.setSummeAllePkw(BigDecimal.valueOf(11));
        interval4.setSummeLastzug(BigDecimal.valueOf(12));
        interval4.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval4.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval4.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        subintervalCollector.getSubIntervals().addAll(List.of(interval1, interval2, interval3, interval4));

        final var result = subintervalCollector.unificationOfCollectedSubintervals();
        final var expected = new IntervalModel();
        expected.setMqId(999);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 11, 0, 0));
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 3, 3, 12, 0, 0));
        expected.setAnzahlLfw(BigDecimal.valueOf(312));
        expected.setAnzahlKrad(BigDecimal.valueOf(416));
        expected.setAnzahlLkw(BigDecimal.valueOf(520));
        expected.setAnzahlBus(BigDecimal.valueOf(832));
        expected.setAnzahlRad(BigDecimal.valueOf(1040));
        expected.setSummeAllePkw(BigDecimal.valueOf(1144));
        expected.setSummeLastzug(BigDecimal.valueOf(1248));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(1352));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(1456));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(1560));

        assertThat(result, is(expected));
    }

    @Test
    void sumSubintervals() {
        final var interval1 = new IntervalModel();
        interval1.setAnzahlLfw(BigDecimal.valueOf(3));
        interval1.setAnzahlKrad(BigDecimal.valueOf(4));
        interval1.setAnzahlLkw(BigDecimal.valueOf(5));
        interval1.setAnzahlBus(BigDecimal.valueOf(8));
        interval1.setAnzahlRad(BigDecimal.valueOf(10));
        interval1.setSummeAllePkw(BigDecimal.valueOf(11));
        interval1.setSummeLastzug(BigDecimal.valueOf(12));
        interval1.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval1.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval2 = new IntervalModel();
        interval2.setAnzahlLfw(BigDecimal.valueOf(3));
        interval2.setAnzahlKrad(BigDecimal.valueOf(4));
        interval2.setAnzahlLkw(BigDecimal.valueOf(5));
        interval2.setAnzahlBus(BigDecimal.valueOf(8));
        interval2.setAnzahlRad(BigDecimal.valueOf(10));
        interval2.setSummeAllePkw(BigDecimal.valueOf(11));
        interval2.setSummeLastzug(BigDecimal.valueOf(12));
        interval2.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval2.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval3 = new IntervalModel();
        interval3.setAnzahlLfw(BigDecimal.valueOf(3));
        interval3.setAnzahlKrad(BigDecimal.valueOf(4));
        interval3.setAnzahlLkw(BigDecimal.valueOf(5));
        interval3.setAnzahlBus(BigDecimal.valueOf(8));
        interval3.setAnzahlRad(BigDecimal.valueOf(10));
        interval3.setSummeAllePkw(BigDecimal.valueOf(11));
        interval3.setSummeLastzug(BigDecimal.valueOf(12));
        interval3.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval3.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval3.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var interval4 = new IntervalModel();
        interval4.setAnzahlLfw(BigDecimal.valueOf(3));
        interval4.setAnzahlKrad(BigDecimal.valueOf(4));
        interval4.setAnzahlLkw(BigDecimal.valueOf(5));
        interval4.setAnzahlBus(BigDecimal.valueOf(8));
        interval4.setAnzahlRad(BigDecimal.valueOf(10));
        interval4.setSummeAllePkw(BigDecimal.valueOf(11));
        interval4.setSummeLastzug(BigDecimal.valueOf(12));
        interval4.setSummeGueterverkehr(BigDecimal.valueOf(13));
        interval4.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        interval4.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        final var subintervalCollector = new SubintervalCollectorModel(null);
        subintervalCollector.getSubIntervals().addAll(List.of(interval1, interval2, interval3, interval4));

        final var result = subintervalCollector.sumSubintervals();
        var expected = new IntervalModel();
        expected.setAnzahlLfw(BigDecimal.valueOf(12));
        expected.setAnzahlKrad(BigDecimal.valueOf(16));
        expected.setAnzahlLkw(BigDecimal.valueOf(20));
        expected.setAnzahlBus(BigDecimal.valueOf(32));
        expected.setAnzahlRad(BigDecimal.valueOf(40));
        expected.setSummeAllePkw(BigDecimal.valueOf(44));
        expected.setSummeLastzug(BigDecimal.valueOf(48));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(52));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(56));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(60));

        assertThat(result, is(expected));
    }

    public SubintervalCollectorModel createSubintervalCollectorModel(final LocalDateTime start, final LocalDateTime end) {
        final var interval = new IntervalModel();
        interval.setTagesTyp(DaveTagesTyp.DTV_W5);
        interval.setMqId(111);
        interval.setDatumUhrzeitVon(start);
        interval.setDatumUhrzeitBis(end);
        return new SubintervalCollectorModel(interval);
    }

}
