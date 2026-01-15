package de.muenchen.dave.geodataeai.domain.service.tagesaggregat;

import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatResponseModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.ZeitraumModel;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TagesaggregatAveragingServiceTest {

    @Mock
    private TagesaggregatExtractionService tagesaggregatExtractionService;

    private TagesaggregatAveragingService tagesaggregatAveragingService;

    @BeforeEach
    public void beforeEach() {
        this.tagesaggregatAveragingService = new TagesaggregatAveragingService(tagesaggregatExtractionService);
        Mockito.reset(tagesaggregatExtractionService);
    }

    @Test
    void getMeanOfAggregatesForAllMqIds() throws FeatureRequestFailedException {
        final LocalDate startDate = LocalDate.of(2024, 2, 1);
        final LocalDate endDate = LocalDate.of(2024, 2, 5);
        int days = 0;
        for (var messtag = startDate; messtag.isBefore(endDate.plusDays(1)); messtag = messtag.plusDays(1)) {
            days += messtag.getDayOfMonth();
        }
        final int includedDays = startDate.until(endDate.plusDays(1)).getDays();
        days = days / includedDays;
        final var tagesaggregate = createTagesaggregate(
                List.of(123, 456, 789),
                startDate,
                endDate,
                DaveTagesTyp.DTV_W5);

        Mockito.when(tagesaggregatExtractionService.getTagesaggregate(
                List.of(123, 456, 789),
                List.of(new ZeitraumModel(startDate, endDate)),
                DaveTagesTyp.DTV_W5)).thenReturn(tagesaggregate.stream());

        final var tagesaggregatRequest = new TagesaggregatRequestModel();
        tagesaggregatRequest.setMessquerschnittIds(List.of(123, 456, 789));
        tagesaggregatRequest.setZeitraeume(List.of(new ZeitraumModel(startDate, endDate)));
        tagesaggregatRequest.setTagesTyp(DaveTagesTyp.DTV_W5);

        final var result = tagesaggregatAveragingService.getMeanOfAggregatesForAllMqIds(tagesaggregatRequest);

        final var expected = new TagesaggregatResponseModel();
        final var expectedForEachMqId = new ArrayList<TagesaggregatModel>();

        var expectedTagesaggregat = new TagesaggregatModel();
        expectedTagesaggregat.setMqId(123);
        expectedTagesaggregat.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedTagesaggregat.setAnzahlLfw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlKrad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlLkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlBus(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlRad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeAllePkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeLastzug(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeGueterverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeSchwerverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setIncludedMeasuringDays((long) includedDays);
        expectedForEachMqId.add(expectedTagesaggregat);

        expectedTagesaggregat = new TagesaggregatModel();
        expectedTagesaggregat.setMqId(456);
        expectedTagesaggregat.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedTagesaggregat.setAnzahlLfw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlKrad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlLkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlBus(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlRad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeAllePkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeLastzug(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeGueterverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeSchwerverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setIncludedMeasuringDays((long) includedDays);
        expectedForEachMqId.add(expectedTagesaggregat);

        expectedTagesaggregat = new TagesaggregatModel();
        expectedTagesaggregat.setMqId(789);
        expectedTagesaggregat.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedTagesaggregat.setAnzahlLfw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlKrad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlLkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlBus(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlRad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeAllePkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeLastzug(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeGueterverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeSchwerverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setIncludedMeasuringDays((long) includedDays);
        expectedForEachMqId.add(expectedTagesaggregat);
        expected.setMeanOfAggregatesForEachMqId(expectedForEachMqId);

        final var meansForAllMqId = new TagesaggregatModel();
        meansForAllMqId.setMqId(789);
        meansForAllMqId.setDatum(LocalDateTime.of(2024, 2, 5, 0, 0, 0));
        meansForAllMqId.setTagesTyp(DaveTagesTyp.DTV_W5);
        final BigDecimal expectedValue = BigDecimal.valueOf(1377);
        meansForAllMqId.setAnzahlLfw(expectedValue);
        meansForAllMqId.setAnzahlKrad(expectedValue);
        meansForAllMqId.setAnzahlLkw(expectedValue);
        meansForAllMqId.setAnzahlBus(expectedValue);
        meansForAllMqId.setAnzahlRad(expectedValue);
        meansForAllMqId.setSummeAllePkw(expectedValue);
        meansForAllMqId.setSummeLastzug(expectedValue);
        meansForAllMqId.setSummeGueterverkehr(expectedValue);
        meansForAllMqId.setSummeSchwerverkehr(expectedValue);
        meansForAllMqId.setSummeKraftfahrzeugverkehr(expectedValue);
        meansForAllMqId.setIncludedMeasuringDays((long) includedDays);
        expected.setSumOverAllAggregatesOfAllMqId(meansForAllMqId);

        Assertions.assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("meanOfAggregatesForEachMqId.datum", "sumOverAllAggregatesOfAllMqId.datum")
                .ignoringCollectionOrder()
                .isEqualTo(expected);

        Mockito.verify(tagesaggregatExtractionService, Mockito.times(1)).getTagesaggregate(
                List.of(123, 456, 789),
                List.of(new ZeitraumModel(startDate, endDate)),
                DaveTagesTyp.DTV_W5);
    }

    @Test
    void calculateMeanOfAggregatesForEachMessquerschnitt() {
        final LocalDate startDate = LocalDate.of(2024, 2, 1);
        final LocalDate endDate = LocalDate.of(2024, 2, 5);
        int days = 0;
        for (var messtag = startDate; messtag.isBefore(endDate.plusDays(1)); messtag = messtag.plusDays(1)) {
            days += messtag.getDayOfMonth();
        }
        final int includedDays = startDate.until(endDate.plusDays(1)).getDays();
        days = days / includedDays;
        final var tagesaggregate = createTagesaggregate(
                List.of(123, 456, 789),
                startDate,
                endDate,
                DaveTagesTyp.DTV_W5);

        final var result = tagesaggregatAveragingService.calculateMeanOfAggregatesForEachMessquerschnitt(tagesaggregate);
        final var expected = new ArrayList<TagesaggregatModel>();

        var expectedTagesaggregat = new TagesaggregatModel();
        expectedTagesaggregat.setMqId(123);
        expectedTagesaggregat.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedTagesaggregat.setAnzahlLfw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlKrad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlLkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlBus(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlRad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeAllePkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeLastzug(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeGueterverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeSchwerverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setIncludedMeasuringDays((long) includedDays);
        expected.add(expectedTagesaggregat);

        expectedTagesaggregat = new TagesaggregatModel();
        expectedTagesaggregat.setMqId(456);
        expectedTagesaggregat.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedTagesaggregat.setAnzahlLfw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlKrad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlLkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlBus(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlRad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeAllePkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeLastzug(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeGueterverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeSchwerverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setIncludedMeasuringDays((long) includedDays);
        expected.add(expectedTagesaggregat);

        expectedTagesaggregat = new TagesaggregatModel();
        expectedTagesaggregat.setMqId(789);
        expectedTagesaggregat.setTagesTyp(DaveTagesTyp.DTV_W5);
        expectedTagesaggregat.setAnzahlLfw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlKrad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlLkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlBus(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setAnzahlRad(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeAllePkw(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeLastzug(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeGueterverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeSchwerverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setSummeKraftfahrzeugverkehr(BigDecimal.valueOf(expectedTagesaggregat.getMqId() + days));
        expectedTagesaggregat.setIncludedMeasuringDays((long) includedDays);
        expected.add(expectedTagesaggregat);

        Assertions.assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("datum")
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    private List<TagesaggregatModel> createTagesaggregate(
            final List<Integer> messquerschnittIds,
            final LocalDate startDate,
            final LocalDate endDate,
            final DaveTagesTyp tagesTyp) {
        final var aggregates = new ArrayList<TagesaggregatModel>();
        for (var messtag = startDate; messtag.isBefore(endDate.plusDays(1)); messtag = messtag.plusDays(1)) {
            for (final var mqId : messquerschnittIds) {
                final BigDecimal countValue = BigDecimal.valueOf(mqId + messtag.getDayOfMonth());
                final TagesaggregatModel model = new TagesaggregatModel();
                model.setMqId(mqId);
                model.setDatum(messtag.atTime(LocalTime.MIDNIGHT));
                model.setTagesTyp(tagesTyp);
                model.setAnzahlLfw(countValue);
                model.setAnzahlKrad(countValue);
                model.setAnzahlLkw(countValue);
                model.setAnzahlBus(countValue);
                model.setAnzahlRad(countValue);
                model.setSummeAllePkw(countValue);
                model.setSummeLastzug(countValue);
                model.setSummeGueterverkehr(countValue);
                model.setSummeSchwerverkehr(countValue);
                model.setSummeKraftfahrzeugverkehr(countValue);
                model.setIncludedMeasuringDays((long) startDate.until(endDate).getDays());
                aggregates.add(model);
            }
        }
        return aggregates;
    }

}
