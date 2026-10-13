package de.muenchen.dave.geodataeai.domain.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MesswertUtilsTest {

    @Test
    void divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull() {
        var result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(null, null);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.TEN, null);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.TEN, 0L);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(null, 10L);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.valueOf(20), 3L);
        assertThat(result, is(BigDecimal.valueOf(7)));

        result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.valueOf(20), 6L);
        assertThat(result, is(BigDecimal.valueOf(3)));

        result = MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.valueOf(20), 4L);
        assertThat(result, is(BigDecimal.valueOf(5)));
    }

    @Test
    void divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull() {
        var result = MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(null, null);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.TEN, null);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.TEN, BigDecimal.valueOf(0, 100));
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(null, BigDecimal.TEN);
        assertThat(result, is(nullValue()));

        result = MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.valueOf(14), BigDecimal.valueOf(680));
        assertThat(result, is(BigDecimal.valueOf(2.1)));

        result = MesswertUtils.divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(BigDecimal.valueOf(27), BigDecimal.valueOf(666));
        assertThat(result, is(BigDecimal.valueOf(4.1)));
    }

    @Test
    void sumCountingValuesOfIntervalsAndReturnNewInterval() {
        var interval1 = new IntervalModel();
        var interval2 = new IntervalModel();
        var result = MesswertUtils.sumCountingValuesOfIntervalsAndReturnNewInterval(interval1, interval2);
        var expected = new IntervalModel();

        assertThat(result, is(expected));

        interval1 = new IntervalModel();
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

        interval2 = new IntervalModel();
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

        result = MesswertUtils.sumCountingValuesOfIntervalsAndReturnNewInterval(interval1, interval2);
        expected = new IntervalModel();
        expected.setAnzahlLfw(BigDecimal.valueOf(6));
        expected.setAnzahlKrad(BigDecimal.valueOf(8));
        expected.setAnzahlLkw(BigDecimal.valueOf(10));
        expected.setAnzahlBus(BigDecimal.valueOf(16));
        expected.setAnzahlRad(BigDecimal.valueOf(20));
        expected.setSummeAllePkw(BigDecimal.valueOf(22));
        expected.setSummeLastzug(BigDecimal.valueOf(24));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(26));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(28));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(30));

        assertThat(result, is(expected));
    }

    @Test
    void sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModel() {
        var tagesaggregat1 = new TagesaggregatModel();
        var tagesaggregat2 = new TagesaggregatModel();
        var result = MesswertUtils.sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModel(tagesaggregat1, tagesaggregat2);
        var expected = new TagesaggregatModel();

        assertThat(result, is(expected));

        tagesaggregat1 = new TagesaggregatModel();
        tagesaggregat1.setAnzahlLfw(BigDecimal.valueOf(3));
        tagesaggregat1.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat1.setAnzahlLkw(BigDecimal.valueOf(5));
        tagesaggregat1.setAnzahlBus(BigDecimal.valueOf(8));
        tagesaggregat1.setAnzahlRad(BigDecimal.valueOf(10));
        tagesaggregat1.setSummeAllePkw(BigDecimal.valueOf(11));
        tagesaggregat1.setSummeLastzug(BigDecimal.valueOf(12));
        tagesaggregat1.setSummeGueterverkehr(BigDecimal.valueOf(13));
        tagesaggregat1.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        tagesaggregat1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        tagesaggregat2 = new TagesaggregatModel();
        tagesaggregat2.setAnzahlLfw(BigDecimal.valueOf(3));
        tagesaggregat2.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat2.setAnzahlLkw(BigDecimal.valueOf(5));
        tagesaggregat2.setAnzahlBus(BigDecimal.valueOf(8));
        tagesaggregat2.setAnzahlRad(BigDecimal.valueOf(10));
        tagesaggregat2.setSummeAllePkw(BigDecimal.valueOf(11));
        tagesaggregat2.setSummeLastzug(BigDecimal.valueOf(12));
        tagesaggregat2.setSummeGueterverkehr(BigDecimal.valueOf(13));
        tagesaggregat2.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        tagesaggregat2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        result = MesswertUtils.sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModel(tagesaggregat1, tagesaggregat2);
        expected = new TagesaggregatModel();
        expected.setAnzahlLfw(BigDecimal.valueOf(6));
        expected.setAnzahlKrad(BigDecimal.valueOf(8));
        expected.setAnzahlLkw(BigDecimal.valueOf(10));
        expected.setAnzahlBus(BigDecimal.valueOf(16));
        expected.setAnzahlRad(BigDecimal.valueOf(20));
        expected.setSummeAllePkw(BigDecimal.valueOf(22));
        expected.setSummeLastzug(BigDecimal.valueOf(24));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(26));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(28));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(30));

        assertThat(result, is(expected));
    }

    @Test
    void sumValuesTreatingNullAsZeroOrReturnNullIfAllNullBigDecimal() {
        var value1 = BigDecimal.valueOf(1);
        var value2 = BigDecimal.valueOf(2);
        var value3 = BigDecimal.valueOf(3);
        var result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(BigDecimal.valueOf(6)));

        value1 = BigDecimal.valueOf(1);
        value2 = null;
        value3 = null;
        result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(BigDecimal.valueOf(1)));

        value1 = null;
        value2 = BigDecimal.valueOf(1);
        value3 = null;
        result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(BigDecimal.valueOf(1)));

        value1 = null;
        value2 = null;
        value3 = BigDecimal.valueOf(1);
        result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(BigDecimal.valueOf(1)));

        value1 = BigDecimal.valueOf(1);
        value2 = BigDecimal.valueOf(2);
        value3 = null;
        result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(BigDecimal.valueOf(3)));

        value1 = null;
        value2 = BigDecimal.valueOf(1);
        value3 = BigDecimal.valueOf(2);
        result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(BigDecimal.valueOf(3)));

        value1 = null;
        value2 = null;
        value3 = null;
        result = MesswertUtils.sumValuesTreatingNullAsZeroOrReturnNullIfAllNull(value1, value2, value3);
        assertThat(result, is(nullValue()));
    }

    @Test
    void sumAggregatesAndAdaptDatumAndMqIdAndReturnNewTagesaggregatModel() {
        var tagesaggregat1 = new TagesaggregatModel();
        var tagesaggregat2 = new TagesaggregatModel();
        var result = MesswertUtils.sumAggregatesAndAdaptDatumAndMqIdAndReturnNewTagesaggregatModel(tagesaggregat1, tagesaggregat2);
        var expected = new TagesaggregatModel();

        assertThat(result, is(expected));

        tagesaggregat1 = new TagesaggregatModel();
        tagesaggregat1.setAnzahlLfw(BigDecimal.valueOf(3));
        tagesaggregat1.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat1.setAnzahlLkw(BigDecimal.valueOf(5));
        tagesaggregat1.setAnzahlBus(BigDecimal.valueOf(8));
        tagesaggregat1.setAnzahlRad(BigDecimal.valueOf(10));
        tagesaggregat1.setSummeAllePkw(BigDecimal.valueOf(11));
        tagesaggregat1.setSummeLastzug(BigDecimal.valueOf(12));
        tagesaggregat1.setSummeGueterverkehr(BigDecimal.valueOf(13));
        tagesaggregat1.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        tagesaggregat1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        tagesaggregat2 = new TagesaggregatModel();
        tagesaggregat2.setMqId(22);
        tagesaggregat2.setTagesTyp(DaveTagesTyp.WERKTAG_FERIEN);
        tagesaggregat2.setDatum(LocalDateTime.of(2024, 10, 10, 10, 0, 0));
        tagesaggregat2.setAnzahlLfw(BigDecimal.valueOf(3));
        tagesaggregat2.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat2.setAnzahlLkw(BigDecimal.valueOf(5));
        tagesaggregat2.setAnzahlBus(BigDecimal.valueOf(8));
        tagesaggregat2.setAnzahlRad(BigDecimal.valueOf(10));
        tagesaggregat2.setSummeAllePkw(BigDecimal.valueOf(11));
        tagesaggregat2.setSummeLastzug(BigDecimal.valueOf(12));
        tagesaggregat2.setSummeGueterverkehr(BigDecimal.valueOf(13));
        tagesaggregat2.setSummeSchwerverkehr(BigDecimal.valueOf(14));
        tagesaggregat2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(15));

        result = MesswertUtils.sumAggregatesAndAdaptDatumAndMqIdAndReturnNewTagesaggregatModel(tagesaggregat1, tagesaggregat2);
        expected = new TagesaggregatModel();
        expected.setMqId(22);
        expected.setTagesTyp(DaveTagesTyp.WERKTAG_FERIEN);
        expected.setDatum(LocalDateTime.of(2024, 10, 10, 10, 0, 0));
        expected.setAnzahlLfw(BigDecimal.valueOf(6));
        expected.setAnzahlKrad(BigDecimal.valueOf(8));
        expected.setAnzahlLkw(BigDecimal.valueOf(10));
        expected.setAnzahlBus(BigDecimal.valueOf(16));
        expected.setAnzahlRad(BigDecimal.valueOf(20));
        expected.setSummeAllePkw(BigDecimal.valueOf(22));
        expected.setSummeLastzug(BigDecimal.valueOf(24));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(26));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(28));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(30));

        assertThat(result, is(expected));
    }

    @Test
    void avarageTagesaggregatCountingValuesByNumberOfElements() {
        final var tagesaggregat1 = new TagesaggregatModel();
        tagesaggregat1.setAnzahlLfw(BigDecimal.valueOf(2));
        tagesaggregat1.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat1.setAnzahlLkw(null);
        tagesaggregat1.setAnzahlBus(BigDecimal.valueOf(7));
        tagesaggregat1.setAnzahlRad(BigDecimal.valueOf(9));
        tagesaggregat1.setSummeAllePkw(BigDecimal.valueOf(10));
        tagesaggregat1.setSummeLastzug(BigDecimal.valueOf(11));
        tagesaggregat1.setSummeGueterverkehr(BigDecimal.valueOf(12));
        tagesaggregat1.setSummeSchwerverkehr(BigDecimal.valueOf(13));
        tagesaggregat1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(14));

        final var tagesaggregat2 = new TagesaggregatModel();
        tagesaggregat2.setAnzahlLfw(BigDecimal.valueOf(2));
        tagesaggregat2.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat2.setAnzahlLkw(null);
        tagesaggregat2.setAnzahlBus(BigDecimal.valueOf(7));
        tagesaggregat2.setAnzahlRad(BigDecimal.valueOf(9));
        tagesaggregat2.setSummeAllePkw(BigDecimal.valueOf(10));
        tagesaggregat2.setSummeLastzug(BigDecimal.valueOf(11));
        tagesaggregat2.setSummeGueterverkehr(BigDecimal.valueOf(12));
        tagesaggregat2.setSummeSchwerverkehr(BigDecimal.valueOf(13));
        tagesaggregat2.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(14));

        final var tagesaggregat3 = new TagesaggregatModel();
        tagesaggregat3.setAnzahlLfw(BigDecimal.valueOf(2));
        tagesaggregat3.setAnzahlKrad(BigDecimal.valueOf(4));
        tagesaggregat3.setAnzahlLkw(null);
        tagesaggregat3.setAnzahlBus(BigDecimal.valueOf(7));
        tagesaggregat3.setAnzahlRad(BigDecimal.valueOf(9));
        tagesaggregat3.setSummeAllePkw(BigDecimal.valueOf(10));
        tagesaggregat3.setSummeLastzug(BigDecimal.valueOf(11));
        tagesaggregat3.setSummeGueterverkehr(BigDecimal.valueOf(12));
        tagesaggregat3.setSummeSchwerverkehr(BigDecimal.valueOf(13));
        tagesaggregat3.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(14));

        final var result = MesswertUtils.avarageTagesaggregatCountingValuesByNumberOfElements(
                List.of(tagesaggregat1, tagesaggregat2, tagesaggregat3));

        final var expected = new TagesaggregatModel();
        expected.setAnzahlLfw(BigDecimal.valueOf(2));
        expected.setAnzahlKrad(BigDecimal.valueOf(4));
        expected.setAnzahlLkw(null);
        expected.setAnzahlBus(BigDecimal.valueOf(7));
        expected.setAnzahlRad(BigDecimal.valueOf(9));
        expected.setSummeAllePkw(BigDecimal.valueOf(10));
        expected.setSummeLastzug(BigDecimal.valueOf(11));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(12));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(13));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(14));
        expected.setIncludedMeasuringDays(3L);

        assertThat(result, is(expected));

    }

    @Test
    void divideTagesaggregatCountingValuesByCounter() {
        final var tagesaggregat1 = new TagesaggregatModel();
        tagesaggregat1.setAnzahlLfw(BigDecimal.valueOf(11));
        tagesaggregat1.setAnzahlKrad(BigDecimal.valueOf(12));
        tagesaggregat1.setAnzahlLkw(null);
        tagesaggregat1.setAnzahlBus(BigDecimal.valueOf(21));
        tagesaggregat1.setAnzahlRad(BigDecimal.valueOf(27));
        tagesaggregat1.setSummeAllePkw(BigDecimal.valueOf(30));
        tagesaggregat1.setSummeLastzug(BigDecimal.valueOf(33));
        tagesaggregat1.setSummeGueterverkehr(BigDecimal.valueOf(36));
        tagesaggregat1.setSummeSchwerverkehr(BigDecimal.valueOf(39));
        tagesaggregat1.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(42));

        final var result = MesswertUtils.divideTagesaggregatCountingValuesByCounter(tagesaggregat1, 3);

        final var expected = new TagesaggregatModel();
        expected.setAnzahlLfw(BigDecimal.valueOf(4));
        expected.setAnzahlKrad(BigDecimal.valueOf(4));
        expected.setAnzahlLkw(null);
        expected.setAnzahlBus(BigDecimal.valueOf(7));
        expected.setAnzahlRad(BigDecimal.valueOf(9));
        expected.setSummeAllePkw(BigDecimal.valueOf(10));
        expected.setSummeLastzug(BigDecimal.valueOf(11));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(12));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(13));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(14));
        expected.setIncludedMeasuringDays(3L);

        assertThat(result, is(expected));
    }

    @Test
    void sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval() {
        var interval1 = new IntervalModel();
        var interval2 = new IntervalModel();
        var result = MesswertUtils.sumCountingValuesOfIntervalsAndReturnNewInterval(interval1, interval2);
        var expected = new IntervalModel();

        assertThat(result, is(expected));

        interval1 = new IntervalModel();
        interval1.setMqId(null);
        interval1.setTagesTyp(null);
        interval1.setDatumUhrzeitVon(LocalDateTime.now());
        interval1.setDatumUhrzeitBis(LocalDateTime.now().plusMinutes(30));
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

        interval2 = new IntervalModel();
        interval2.setMqId(99);
        interval2.setTagesTyp(DaveTagesTyp.DTV_W5);
        interval2.setDatumUhrzeitVon(interval1.getDatumUhrzeitVon().plusMinutes(2));
        interval2.setDatumUhrzeitBis(interval1.getDatumUhrzeitBis().plusMinutes(2));
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

        result = MesswertUtils.sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval(interval1, interval2);
        expected = new IntervalModel();
        expected.setMqId(99);
        expected.setTagesTyp(DaveTagesTyp.DTV_W5);
        expected.setDatumUhrzeitVon(interval1.getDatumUhrzeitVon());
        expected.setDatumUhrzeitBis(interval2.getDatumUhrzeitBis());
        expected.setAnzahlLfw(BigDecimal.valueOf(6));
        expected.setAnzahlKrad(BigDecimal.valueOf(8));
        expected.setAnzahlLkw(BigDecimal.valueOf(10));
        expected.setAnzahlBus(BigDecimal.valueOf(16));
        expected.setAnzahlRad(BigDecimal.valueOf(20));
        expected.setSummeAllePkw(BigDecimal.valueOf(22));
        expected.setSummeLastzug(BigDecimal.valueOf(24));
        expected.setSummeGueterverkehr(BigDecimal.valueOf(26));
        expected.setSummeSchwerverkehr(BigDecimal.valueOf(28));
        expected.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(30));

        assertThat(result, is(expected));
    }

    @Test
    void setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(null);
        interval.setDatumUhrzeitBis(null);
        MesswertUtils.setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay(interval);
        final var expected = new IntervalModel();
        expected.setDatumUhrzeitVon(null);
        expected.setDatumUhrzeitBis(null);
        assertThat(interval, is(expected));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 29, 10, 0, 0));
        interval.setDatumUhrzeitBis(null);
        MesswertUtils.setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay(interval);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 29, 10, 0, 0));
        expected.setDatumUhrzeitBis(null);
        assertThat(interval, is(expected));

        interval.setDatumUhrzeitVon(null);
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 29, 10, 0, 0));
        MesswertUtils.setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay(interval);
        expected.setDatumUhrzeitVon(null);
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 29, 10, 0, 0));
        assertThat(interval, is(expected));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 29, 9, 15, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 29, 10, 0, 0));
        MesswertUtils.setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay(interval);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 29, 9, 15, 0));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 29, 10, 0, 0));
        assertThat(interval, is(expected));

        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 29, 23, 45, 0));
        interval.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 29, 0, 0, 0));
        MesswertUtils.setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay(interval);
        expected.setDatumUhrzeitVon(LocalDateTime.of(2024, 2, 29, 23, 45, 0));
        expected.setDatumUhrzeitBis(LocalDateTime.of(2024, 2, 29, 23, 59, 59));
        assertThat(interval, is(expected));
    }

    @Test
    void getStartDateFromInterval() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 1, 2, 10, 15, 11));

        var result = MesswertUtils.getStartDateFromInterval(interval);
        var expected = LocalDate.of(2024, 1, 2);
        assertThat(result, is(expected));

        interval.setDatumUhrzeitVon(null);
        result = MesswertUtils.getStartDateFromInterval(interval);
        assertThat(result, is(nullValue()));
    }

    @Test
    void getStartTimeFromInterval() {
        final var interval = new IntervalModel();
        interval.setDatumUhrzeitVon(LocalDateTime.of(2024, 1, 2, 10, 15, 11));

        var result = MesswertUtils.getStartTimeFromInterval(interval);
        var expected = LocalTime.of(10, 15, 11);
        assertThat(result, is(expected));

        interval.setDatumUhrzeitVon(null);
        result = MesswertUtils.getStartTimeFromInterval(interval);
        assertThat(result, is(nullValue()));
    }

    @Test
    void getMin() {
        var date1 = LocalDateTime.now();
        var date2 = date1.plusMinutes(30);

        var result = MesswertUtils.getMin(date1, date2);
        var expected = date1;
        assertThat(result, is(expected));

        result = MesswertUtils.getMin(date2, date1);
        expected = date1;
        assertThat(result, is(expected));

        result = MesswertUtils.getMin(date1, date1);
        expected = date1;
        assertThat(result, is(expected));

        result = MesswertUtils.getMin(null, date2);
        expected = date2;
        assertThat(result, is(expected));

        result = MesswertUtils.getMin(null, date1);
        expected = date1;
        assertThat(result, is(expected));

        result = MesswertUtils.getMin(null, null);
        assertThat(result, is(nullValue()));
    }

    @Test
    void getMax() {
        var date1 = LocalDateTime.now();
        var date2 = date1.plusMinutes(30);

        var result = MesswertUtils.getMax(date1, date2);
        var expected = date2;
        assertThat(result, is(expected));

        result = MesswertUtils.getMax(date2, date1);
        expected = date2;
        assertThat(result, is(expected));

        result = MesswertUtils.getMax(date2, date2);
        expected = date2;
        assertThat(result, is(expected));

        result = MesswertUtils.getMax(null, date2);
        expected = date2;
        assertThat(result, is(expected));

        result = MesswertUtils.getMax(null, date1);
        expected = date1;
        assertThat(result, is(expected));

        result = MesswertUtils.getMax(null, null);
        assertThat(result, is(nullValue()));
    }

}
