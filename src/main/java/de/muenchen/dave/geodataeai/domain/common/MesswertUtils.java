package de.muenchen.dave.geodataeai.domain.common;

import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MesswertUtils {

    private static final BigDecimal FACTOR_PERCENTAGE_CALCULATION = BigDecimal.valueOf(100);

    private static final Integer SCALE_DIVISION = 4;

    private static final Integer SCALE_RESULT_PERCENTAGE_CALCULATION = 1;

    public static BigDecimal divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(final BigDecimal dividend, final Long divisor) {
        return ObjectUtils.allNotNull(dividend, divisor) && divisor != 0
                ? dividend.divide(BigDecimal.valueOf(divisor), 0, RoundingMode.HALF_UP)
                : null;
    }

    public static BigDecimal divideAndRoundHalfUpWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(final BigDecimal dividend, final BigDecimal divisor) {
        BigDecimal result = null;
        if (ObjectUtils.allNotNull(dividend, divisor) && BigDecimal.ZERO.compareTo(divisor) != 0) {
            result = dividend
                    .divide(divisor, SCALE_DIVISION, RoundingMode.HALF_UP)
                    .multiply(FACTOR_PERCENTAGE_CALCULATION)
                    .setScale(SCALE_RESULT_PERCENTAGE_CALCULATION, RoundingMode.HALF_UP);
        }
        return result;
    }

    public static IntervalModel sumCountingValuesOfIntervalsAndReturnNewInterval(
            final IntervalModel interval1,
            final IntervalModel interval2) {
        return (IntervalModel) sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModelForMesswerte(interval1, interval2);
    }

    public static TagesaggregatModel sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModel(
            final TagesaggregatModel tagesaggregat1,
            final TagesaggregatModel tagesaggregat2) {
        return (TagesaggregatModel) sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModelForMesswerte(tagesaggregat1, tagesaggregat2);
    }

    private static MesswertModel sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModelForMesswerte(
            final MesswertModel messwert1,
            final MesswertModel messwert2) {
        final var messwert = TagesaggregatModel.class.equals(messwert1.getClass()) ? new TagesaggregatModel() : new IntervalModel();
        messwert.setAnzahlLfw(sumValuesIfAnyNotNullOrReturnNull(messwert1.getAnzahlLfw(), messwert2.getAnzahlLfw()));
        messwert.setAnzahlKrad(sumValuesIfAnyNotNullOrReturnNull(messwert1.getAnzahlKrad(), messwert2.getAnzahlKrad()));
        messwert.setAnzahlLkw(sumValuesIfAnyNotNullOrReturnNull(messwert1.getAnzahlLkw(), messwert2.getAnzahlLkw()));
        messwert.setAnzahlBus(sumValuesIfAnyNotNullOrReturnNull(messwert1.getAnzahlBus(), messwert2.getAnzahlBus()));
        messwert.setAnzahlRad(sumValuesIfAnyNotNullOrReturnNull(messwert1.getAnzahlRad(), messwert2.getAnzahlRad()));
        messwert.setSummeAllePkw(sumValuesIfAnyNotNullOrReturnNull(messwert1.getSummeAllePkw(), messwert2.getSummeAllePkw()));
        messwert.setSummeLastzug(sumValuesIfAnyNotNullOrReturnNull(messwert1.getSummeLastzug(), messwert2.getSummeLastzug()));
        messwert.setSummeGueterverkehr(sumValuesIfAnyNotNullOrReturnNull(messwert1.getSummeGueterverkehr(), messwert2.getSummeGueterverkehr()));
        messwert.setSummeSchwerverkehr(sumValuesIfAnyNotNullOrReturnNull(messwert1.getSummeSchwerverkehr(), messwert2.getSummeSchwerverkehr()));
        messwert.setSummeKraftfahrzeugverkehr(
                sumValuesIfAnyNotNullOrReturnNull(messwert1.getSummeKraftfahrzeugverkehr(), messwert2.getSummeKraftfahrzeugverkehr()));
        return messwert;
    }

    public static BigDecimal sumValuesIfAnyNotNullOrReturnNull(final BigDecimal... values) {
        BigDecimal summedValue = null;
        if (ObjectUtils.anyNotNull(values)) {
            summedValue = Stream.of(values)
                    .reduce(BigDecimal.ZERO,
                            (value1, value2) -> ObjectUtils.getIfNull(value1, BigDecimal.ZERO).add(ObjectUtils.getIfNull(value2, BigDecimal.ZERO)));
        }
        return summedValue;
    }

    /**
     * Die Methode berechnet den Durchschnitt der in den Parameter gegebenen Tagesaggregate.
     * <p>
     * Der Messquerschnitt, das Datum und der Tagestyp für das durchschnittliche Tagesaggregate wird aus
     * dem zweiten Tagesaggregat entnommen.
     *
     * @param tagesaggregat1 zum Summieren
     * @param tagesaggregat2 zum Summieren
     * @return ein neues Tagesaggregat mit den durchschnittlichen Werten
     */
    public static TagesaggregatModel sumAggregatesAndAdaptDatumAndMqIdAndReturnNewTagesaggregatModel(
            final TagesaggregatModel tagesaggregat1,
            final TagesaggregatModel tagesaggregat2) {
        final var model = sumCountingValuesOfAggregatesAndReturnNewTagesaggregatModel(tagesaggregat1, tagesaggregat2);
        model.setTagesTyp(tagesaggregat2.getTagesTyp());
        model.setDatum(tagesaggregat2.getDatum());
        model.setMqId(tagesaggregat2.getMqId());
        model.setIncludedMeasuringDays(tagesaggregat2.getIncludedMeasuringDays());
        return model;
    }

    /**
     * Für jeden Zählwert in den im Parameter gegebenen Tagesaggregaten wird je Zählwert der
     * Durchschnitt über alle gegebenen Tagesaggregate gebildet.
     *
     * @param tagesaggregate zur Durchschnittsbildung
     * @return ein Tagesaggregat welches den Durchschnitt eines jeden Zählwerts repräsentiert.
     */
    public static TagesaggregatModel avarageTagesaggregatCountingValuesByNumberOfElements(final List<TagesaggregatModel> tagesaggregate) {
        final var tagesaggregatSummedByMesstage = tagesaggregate
                .parallelStream()
                .reduce(
                        new TagesaggregatModel(),
                        MesswertUtils::sumAggregatesAndAdaptDatumAndMqIdAndReturnNewTagesaggregatModel);

        return divideTagesaggregatCountingValuesByCounter(tagesaggregatSummedByMesstage, tagesaggregate.size());
    }

    protected static TagesaggregatModel divideTagesaggregatCountingValuesByCounter(final TagesaggregatModel model, long counter) {
        model.setAnzahlLfw(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getAnzahlLfw(), counter));
        model.setAnzahlKrad(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getAnzahlKrad(), counter));
        model.setAnzahlLkw(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getAnzahlLkw(), counter));
        model.setAnzahlBus(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getAnzahlBus(), counter));
        model.setAnzahlRad(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getAnzahlRad(), counter));
        model.setSummeAllePkw(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getSummeAllePkw(), counter));
        model.setSummeLastzug(MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getSummeLastzug(), counter));
        model.setSummeGueterverkehr(
                MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getSummeGueterverkehr(), counter));
        model.setSummeSchwerverkehr(
                MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getSummeSchwerverkehr(), counter));
        model.setSummeKraftfahrzeugverkehr(
                MesswertUtils.divideAndRoundHalfUPWithScaleZeroOrReturnNullIfDividendOrDivisorIsNull(model.getSummeKraftfahrzeugverkehr(), counter));
        model.setIncludedMeasuringDays(counter);
        return model;
    }

    /**
     * Die Methode summiert die in den Parameter gegebenen Intervalle.
     * <p>
     * Der Messquerschnitt und der Tagestyp für den summierten Interval
     * wird aus dem zweiten Interval entnommen.
     * <p>
     * Das Attribut {@link IntervalModel#getDatumUhrzeitVon()} des summierten Intervalls wird auf das
     * kleinere gleichlautende Attribut der im Parameter gegebenen Intervalle gesetzt.
     * <p>
     * Das Attribut {@link IntervalModel#getDatumUhrzeitBis()} des summierten Intervalls wird auf das
     * größere gleichlautende Attribut der im Parameter gegebenen Intervalle gesetzt.
     *
     * @param interval1 zum Summieren
     * @param interval2 zum Summieren
     * @return einen neuen Interval mit den summierten Werten
     */
    public static IntervalModel sumIntervalsAndAdaptDatumUhrzeitVonAndBisAndReturnNewInterval(
            final IntervalModel interval1,
            final IntervalModel interval2) {
        final var interval = sumCountingValuesOfIntervalsAndReturnNewInterval(interval1, interval2);
        interval.setMqId(interval2.getMqId());
        interval.setTagesTyp(interval2.getTagesTyp());
        final var intervalVon = getMin(interval1.getDatumUhrzeitVon(), interval2.getDatumUhrzeitVon());
        interval.setDatumUhrzeitVon(intervalVon);
        final var intervalBis = getMax(interval1.getDatumUhrzeitBis(), interval2.getDatumUhrzeitBis());
        interval.setDatumUhrzeitBis(intervalBis);
        return interval;
    }

    public static void setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay(final IntervalModel interval) {
        final var datumUhrzeitVon = interval.getDatumUhrzeitVon();
        final var datumUhrzeitBis = interval.getDatumUhrzeitBis();
        if (ObjectUtils.allNotNull(datumUhrzeitVon, datumUhrzeitBis)
                && datumUhrzeitVon.equals(LocalDateTime.of(datumUhrzeitVon.toLocalDate(), LocalTime.of(23, 45, 0)))
                && datumUhrzeitBis.equals(LocalDateTime.of(datumUhrzeitVon.toLocalDate(), LocalTime.MIN))) {
            final var newDatumUhrzeitBis = LocalDateTime.of(
                    datumUhrzeitBis.toLocalDate(),
                    LocalTime.of(23, 59, 59));
            interval.setDatumUhrzeitBis(newDatumUhrzeitBis);
        }
    }

    public static LocalDate getStartDateFromInterval(final IntervalModel interval) {
        return ObjectUtils.isNotEmpty(interval.getDatumUhrzeitVon())
                ? interval.getDatumUhrzeitVon().toLocalDate()
                : null;
    }

    public static LocalTime getStartTimeFromInterval(final IntervalModel interval) {
        return ObjectUtils.isNotEmpty(interval.getDatumUhrzeitVon())
                ? interval.getDatumUhrzeitVon().toLocalTime()
                : null;
    }

    static LocalDateTime getMin(final LocalDateTime dateTime1, final LocalDateTime dateTime2) {
        return Stream.of(dateTime1, dateTime2)
                .filter(ObjectUtils::isNotEmpty)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    static LocalDateTime getMax(final LocalDateTime dateTime1, final LocalDateTime dateTime2) {
        return Stream.of(dateTime1, dateTime2)
                .filter(ObjectUtils::isNotEmpty)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
