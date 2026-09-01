package de.muenchen.dave.geodataeai.domain.mapper;

import de.muenchen.dave.geodataeai.configuration.MapstructConfiguration;
import de.muenchen.dave.geodataeai.domain.common.MesswertUtils;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import de.muenchen.mobidam.eai.gen.model.LoadMesswerteTimeRangeFzTypenParameterInner;
import de.muenchen.mobidam.eai.gen.model.MqMesswerteDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfiguration.class)
public abstract class MesswerteResponseDomainMapper {

    public static final String DATUM_UHRZEIT_VON = "DATUM_UHRZEIT_VON";

    public static final String DATUM_UHRZEIT_BIS = "DATUM_UHRZEIT_BIS";

    public List<IntervalModel> messwerte2Intervals(final MqMesswerteDto messwerte, final DaveTagesTyp tagesTyp) {
        final var messwerteElementIndex = new MesswerteElementIndex(messwerte.getFormat());
        return ListUtils.emptyIfNull(messwerte.getMessquerschnitte())
                .stream()
                .flatMap(messquerschnitt -> ListUtils.emptyIfNull(messquerschnitt.getIntervalle())
                        .stream()
                        .map(intervalData -> messwert2Interval(
                                messquerschnitt.getMqId(),
                                intervalData,
                                messwerteElementIndex,
                                tagesTyp)))
                .peek(MesswertUtils::setDatumUhrzeitBisTo2359IfGivenIntervalIsLast15MinuteIntervalOfDay)
                .toList();
    }

    protected IntervalModel messwert2Interval(
            final Long mqId,
            final List<String> intervalData,
            final MesswerteElementIndex messwerteElementIndex,
            final DaveTagesTyp tagesTyp) {
        final var intervalParser = new IntervalParser(intervalData, messwerteElementIndex);
        return intervalParser2Interval(intervalParser, mqId.intValue(), tagesTyp);
    }

    @Mapping(target = "objectId", ignore = true)
    @Mapping(target = "mqId", expression = "java( mqId )")
    @Mapping(target = "tagesTyp", expression = "java( tagesTyp )")
    protected abstract IntervalModel intervalParser2Interval(
            final IntervalParser intervalParser,
            @Context final Integer mqId,
            @Context final DaveTagesTyp tagesTyp);

    @Data
    protected static class MesswerteElementIndex {

        private final int indexDatumUhrzeitVon;
        private final int indexDatumUhrzeitBis;
        private final int indexSummeKfzVerkehr;
        private final int indexLkw;
        private final int indexKrad;
        private final int indexRad;
        private final int indexLfw;
        private final int indexBus;
        private final int indexSummeAllePkw;
        private final int indexSummeLastzug;
        private final int indexSummeGueterverkehr;
        private final int indexSummeSchwerverkehr;

        public MesswerteElementIndex(final String format) {
            final var attributes = StringUtils.split(format, StringUtils.SPACE);
            indexDatumUhrzeitVon = ArrayUtils.indexOf(attributes, DATUM_UHRZEIT_VON);
            indexDatumUhrzeitBis = ArrayUtils.indexOf(attributes, DATUM_UHRZEIT_BIS);
            indexSummeKfzVerkehr = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.SUMME_KRAFTFAHRZEUGVERKEHR.toString());
            indexLkw = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.ANZAHL_LKW.toString());
            indexKrad = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.ANZAHL_KRAD.toString());
            indexRad = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.ANZAHL_RAD.toString());
            indexLfw = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.ANZAHL_LFW.toString());
            indexBus = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.ANZAHL_BUS.toString());
            indexSummeAllePkw = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.SUMME_ALLE_PKW.toString());
            indexSummeLastzug = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.SUMME_LASTZUG.toString());
            indexSummeGueterverkehr = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.SUMME_GUETERVERKEHR.toString());
            indexSummeSchwerverkehr = ArrayUtils.indexOf(attributes, LoadMesswerteTimeRangeFzTypenParameterInner.SUMME_SCHWERVERKEHR.toString());
        }

    }

    @Data
    protected static class IntervalParser {

        private static final String MISSING_ATTRIBUTE_VALUE = "NULL";

        private final List<String> intervalData;

        private final MesswerteElementIndex elementIndex;

        public IntervalParser(final List<String> intervalData, final MesswerteElementIndex elementIndex) {
            this.intervalData = intervalData;
            this.elementIndex = elementIndex;
        }

        public LocalDateTime getDatumUhrzeitVon() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexDatumUhrzeitVon());
            return getDateTimeDefaultToNull(data);
        }

        public LocalDateTime getDatumUhrzeitBis() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexDatumUhrzeitBis());
            return getDateTimeDefaultToNull(data);
        }

        public BigDecimal getAnzahlLfw() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexLfw());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getAnzahlKrad() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexKrad());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getAnzahlRad() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexRad());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getAnzahlLkw() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexLkw());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getAnzahlBus() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexBus());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getSummeAllePkw() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexSummeAllePkw());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getSummeLastzug() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexSummeLastzug());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getSummeGueterverkehr() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexSummeGueterverkehr());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getSummeSchwerverkehr() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexSummeSchwerverkehr());
            return getZaehlwertOrDefaultToNull(data);
        }

        public BigDecimal getSummeKraftfahrzeugverkehr() {
            final var data = getFromIntervalDataOrDefaultToNull(elementIndex.getIndexSummeKfzVerkehr());
            return getZaehlwertOrDefaultToNull(data);
        }

        private String getFromIntervalDataOrDefaultToNull(final int index) {
            return index > ArrayUtils.INDEX_NOT_FOUND
                    ? intervalData.get(index)
                    : null;
        }

        private LocalDateTime getDateTimeDefaultToNull(final String dateTime) {
            return Objects.isNull(dateTime)
                    ? null
                    : LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        private BigDecimal getZaehlwertOrDefaultToNull(final String zaehlwert) {
            return Objects.isNull(zaehlwert) || Strings.CS.equals(zaehlwert, MISSING_ATTRIBUTE_VALUE)
                    ? null
                    : BigDecimal.valueOf(Long.parseLong(zaehlwert));
        }

    }

}
