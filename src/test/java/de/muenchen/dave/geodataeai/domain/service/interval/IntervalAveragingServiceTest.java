package de.muenchen.dave.geodataeai.domain.service.interval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntervalAveragingServiceTest {

    private IntervalAveragingService intervalAveragingService = new IntervalAveragingService(new IntervalSummationService());

    @Test
    void averagingOfSummedUpDailyIntervalsOverMesstageForEachMessquerschnitt() {
        final var intervalle = new IntervalSummationServiceTest().createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 3),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalAveragingService
                .averagingOfSummedUpDailyIntervalsOverMesstageForEachMessquerschnitt(intervalle);

        final var expected = new ArrayList<IntervalModel>();

        var expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(58));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(58));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(58));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(58));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(58));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(58));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(58));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(58));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(58));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(58));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(74));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(74));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(74));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(74));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(74));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(74));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(74));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(74));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(74));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(74));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(90));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(90));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(90));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(90));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(90));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(90));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(90));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(90));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(90));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(90));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void averagingOfSummedUpDailyIntervalsOverMesstageForEachMessquerschnittOneDayPeriod() {
        final var intervalle = new IntervalSummationServiceTest().createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalAveragingService
                .averagingOfSummedUpDailyIntervalsOverMesstageForEachMessquerschnitt(intervalle);

        final var expected = new ArrayList<IntervalModel>();

        var expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(10));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(10));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(10));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(10));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(10));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(10));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(26));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(26));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(26));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(26));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(26));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(26));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(26));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(26));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(26));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(26));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(42));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(42));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(42));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(42));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(42));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(42));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(42));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(42));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(42));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(42));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void averagingOfMessquerschnitteForEachIntervalByMesstag() {
        final var intervalle = new IntervalSummationServiceTest().createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 3),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalAveragingService.averagingOfIntervalsOverMessquerschnittAndMesstag(intervalle);

        final var expected = new ArrayList<IntervalModel>();

        var expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(51));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(51));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(51));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(51));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(51));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(51));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(51));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(51));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(51));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(51));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(54));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(54));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(54));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(54));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(54));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(54));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(54));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(54));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(54));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(54));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(57));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(57));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(57));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(57));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(57));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(57));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(57));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(57));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(57));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(57));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(60));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(60));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(60));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(60));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(60));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(60));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(60));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(60));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(60));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(60));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void averagingOfMessquerschnitteForEachIntervalByMesstagOneDayPeriod() {
        final var intervalle = new IntervalSummationServiceTest().createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalAveragingService.averagingOfIntervalsOverMessquerschnittAndMesstag(intervalle);

        final var expected = new ArrayList<IntervalModel>();

        var expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(15));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(15));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(15));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(15));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(15));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(18));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(18));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(18));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(18));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(18));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(18));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(21));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(21));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(21));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(21));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(21));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(21));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(24));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(24));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(24));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(24));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(24));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(24));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void avaragingOfIntervalsAndMessquerschnittOverMesstage() {
        final var intervalle = new IntervalSummationServiceTest().createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 3),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalAveragingService.avaragingOfIntervalsAndMessquerschnittOverMesstage(intervalle);

        final var expected = new ArrayList<IntervalModel>();

        var expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(13));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(13));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(13));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(13));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(13));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(13));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(13));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(13));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(13));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(13));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(14));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(14));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(14));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(14));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(14));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(14));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(14));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(14));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(14));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(15));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(15));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(15));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(15));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(15));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(15));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(16));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(16));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(16));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(16));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(16));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(16));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(16));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(16));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(16));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(16));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(17));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(17));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(17));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(17));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(17));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(17));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(17));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(17));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(17));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(17));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(18));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(18));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(18));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(18));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(18));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(18));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(18));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(19));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(19));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(19));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(19));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(19));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(19));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(19));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(19));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(19));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(19));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(20));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(20));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(20));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(20));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(20));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(20));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(20));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(20));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(20));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(20));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(21));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(21));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(21));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(21));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(21));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(21));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(21));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(22));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(22));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(22));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(22));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(22));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(22));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(22));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(22));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(22));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(22));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(23));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(23));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(23));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(23));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(23));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(23));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(23));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(23));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(23));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(23));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(24));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(24));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(24));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(24));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(24));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(24));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(24));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void avaragingOfIntervalsAndMessquerschnittOverMesstageOneDayPeriod() {
        final var intervalle = new IntervalSummationServiceTest().createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalAveragingService.avaragingOfIntervalsAndMessquerschnittOverMesstage(intervalle);

        final var expected = new ArrayList<IntervalModel>();

        var expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(1));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(1));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(1));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(1));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(1));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(1));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(1));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(1));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(1));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(1));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(2));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(2));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(2));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(2));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(2));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(2));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(2));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(2));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(2));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(2));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(3));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(3));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(3));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(3));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(3));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(3));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(3));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(3));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(3));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(3));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(4));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(4));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(4));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(4));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(4));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(4));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(4));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(4));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(4));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(4));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(5));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(5));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(5));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(5));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(5));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(5));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(5));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(5));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(5));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(5));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(6));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(6));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(6));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(6));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(6));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(6));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(6));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(6));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(6));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(6));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(7));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(7));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(7));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(7));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(7));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(7));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(7));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(7));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(7));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(7));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(8));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(8));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(8));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(8));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(8));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(8));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(8));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(8));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(8));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(8));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(9));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(9));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(9));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(9));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(9));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(9));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(9));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(9));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(9));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(9));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(10));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(10));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(10));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(10));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(10));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(10));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(10));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(11));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(11));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(11));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(11));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(11));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(11));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(11));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(11));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(11));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(11));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 1, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 1, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(12));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(12));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(12));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(12));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(12));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(12));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(12));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(12));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(12));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(12));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

}
