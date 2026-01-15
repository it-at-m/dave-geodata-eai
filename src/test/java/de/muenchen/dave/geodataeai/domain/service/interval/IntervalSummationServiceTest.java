package de.muenchen.dave.geodataeai.domain.service.interval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.dave.geodataeai.TestData;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntervalSummationServiceTest {

    private IntervalSummationService intervalSummationService = new IntervalSummationService();

    @Test
    void summationOfIntervalsForEachMessquerschnittForEachMesstag() {
        final var intervalle = createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 3),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalSummationService.summationOfIntervalsForEachMessquerschnittByMesstag(intervalle);

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

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
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
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
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
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
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

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(401);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(106));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(106));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(106));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(106));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(106));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(106));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(106));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(106));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(106));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(106));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(402);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(122));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(122));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(122));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(122));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(122));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(122));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(122));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(122));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(122));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(122));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(403);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(138));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(138));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(138));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(138));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(138));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(138));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(138));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(138));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(138));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(138));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void summationOfIntervalsForEachMesstagByMessquerschnitt() {
        final var intervalle = createMesswertIntervalle(
                List.of(401, 402, 403),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 3),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                IntervalSize.INTERVAL_15,
                DaveTagesTyp.DTV_W5);

        final var result = intervalSummationService.summationOfIntervalsForEachMesstagByMessquerschnitt(intervalle);

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

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
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
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
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
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 10, 45, 0));
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
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 2, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 2, 11, 0, 0));
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

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 0, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 15, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(87));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(87));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(87));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(87));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(87));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(87));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(87));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(87));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(87));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(87));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 15, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 30, 0));
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

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 30, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 10, 45, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(93));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(93));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(93));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(93));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(93));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(93));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(93));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(93));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(93));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(93));
        expected.add(expectedInterval);

        expectedInterval = new IntervalModel();
        expectedInterval.setMqId(null);
        expectedInterval.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedInterval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 3, 10, 45, 0));
        expectedInterval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 3, 11, 0, 0));
        expectedInterval.setAnzahlLfw(BigDecimal.valueOf(96));
        expectedInterval.setAnzahlKrad(BigDecimal.valueOf(96));
        expectedInterval.setAnzahlLkw(BigDecimal.valueOf(96));
        expectedInterval.setAnzahlBus(BigDecimal.valueOf(96));
        expectedInterval.setAnzahlRad(BigDecimal.valueOf(96));
        expectedInterval.setSummeAllePkw(BigDecimal.valueOf(96));
        expectedInterval.setSummeLastzug(BigDecimal.valueOf(96));
        expectedInterval.setSummeGueterverkehr(BigDecimal.valueOf(96));
        expectedInterval.setSummeSchwerverkehr(BigDecimal.valueOf(96));
        expectedInterval.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(96));
        expected.add(expectedInterval);

        assertThat(result, containsInAnyOrder(expected.toArray()));
    }

    public List<IntervalModel> createMesswertIntervalle(
            final List<Integer> messquerschnittIds,
            final LocalDate startDate,
            final LocalDate endDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final IntervalSize intervalSize,
            final DaveTagesTyp tagesTyp) {
        AtomicLong counter = new AtomicLong(0);
        return TestData.createMesswertIntervalle(
                messquerschnittIds,
                startDate,
                endDate,
                startTime,
                endTime,
                intervalSize,
                tagesTyp).peek(interval -> {
                    final var countValue = BigDecimal.valueOf(counter.incrementAndGet());
                    interval.setAnzahlLfw(countValue);
                    interval.setAnzahlKrad(countValue);
                    interval.setAnzahlLkw(countValue);
                    interval.setAnzahlBus(countValue);
                    interval.setAnzahlRad(countValue);
                    interval.setSummeAllePkw(countValue);
                    interval.setSummeLastzug(countValue);
                    interval.setSummeGueterverkehr(countValue);
                    interval.setSummeSchwerverkehr(countValue);
                    interval.setSummeKraftfahrzeugverkehr(countValue);
                })
                .toList();
    }
}
