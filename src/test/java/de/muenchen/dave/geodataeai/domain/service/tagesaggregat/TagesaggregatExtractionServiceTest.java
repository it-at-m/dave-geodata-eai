package de.muenchen.dave.geodataeai.domain.service.tagesaggregat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.mapper.FeatureResponseDomainMapperImpl;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.ZeitraumModel;
import de.muenchen.dave.geodataeai.infrastructure.client.ArcgisRestClient;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.Feature;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureCollection;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messwerte.Tagesaggregat;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TagesaggregatExtractionServiceTest {

    @Mock
    private ArcgisRestClient arcgisRestClient;

    private TagesaggregatExtractionService tagesaggregatExtractionService;

    @BeforeEach
    public void beforeEach() {
        this.tagesaggregatExtractionService = new TagesaggregatExtractionService(
                arcgisRestClient,
                new FeatureResponseDomainMapperImpl(),
                "url-tagesaggregat");
        Mockito.reset(arcgisRestClient);
    }

    @Test
    void getTagesaggregateForMessquerschnitte() throws FeatureRequestFailedException {
        final var featureCollection = new FeatureCollection<Feature<Tagesaggregat>>();

        var feature1 = new Feature<Tagesaggregat>();
        var tagesaggreget1 = new Tagesaggregat();
        tagesaggreget1.setAnzahlLkw(BigDecimal.valueOf(1));
        feature1.setProperties(tagesaggreget1);

        var feature2 = new Feature<Tagesaggregat>();
        var tagesaggreget2 = new Tagesaggregat();
        tagesaggreget2.setAnzahlLkw(BigDecimal.valueOf(2));
        feature2.setProperties(tagesaggreget2);

        var feature3 = new Feature<Tagesaggregat>();
        var tagesaggreget3 = new Tagesaggregat();
        tagesaggreget3.setAnzahlLkw(BigDecimal.valueOf(3));
        feature3.setProperties(tagesaggreget3);

        featureCollection.setFeatures(List.of(feature1, feature2, feature3));

        Mockito.when(arcgisRestClient.extractFeature(
                "url-tagesaggregat",
                "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-03-01 00:00:00')) AND (TAGESTYP=1)",
                new ParameterizedTypeReference<FeatureCollection<Feature<Tagesaggregat>>>() {
                })).thenReturn(featureCollection);

        final var result = tagesaggregatExtractionService.getTagesaggregate(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29))),
                DaveTagesTyp.DTV_W3)
                .toList();

        var expectedTagesaggregat1 = new TagesaggregatModel();
        expectedTagesaggregat1.setAnzahlLkw(BigDecimal.valueOf(1));
        expectedTagesaggregat1.setTagesTyp(DaveTagesTyp.DTV_W3);
        var expectedTagesaggregat2 = new TagesaggregatModel();
        expectedTagesaggregat2.setAnzahlLkw(BigDecimal.valueOf(2));
        expectedTagesaggregat2.setTagesTyp(DaveTagesTyp.DTV_W3);
        var expectedTagesaggregat3 = new TagesaggregatModel();
        expectedTagesaggregat3.setAnzahlLkw(BigDecimal.valueOf(3));
        expectedTagesaggregat3.setTagesTyp(DaveTagesTyp.DTV_W3);

        final var expected = Stream.of(expectedTagesaggregat1, expectedTagesaggregat2, expectedTagesaggregat3);

        assertThat(result, containsInAnyOrder(expected.toArray()));

        Mockito.verify(arcgisRestClient, Mockito.times(1)).extractFeature(
                "url-tagesaggregat",
                "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-03-01 00:00:00')) AND (TAGESTYP=1)",
                new ParameterizedTypeReference<FeatureCollection<Feature<Tagesaggregat>>>() {
                });
    }

    @Test
    void getTagesaggregateForMessquerschnitteWithoutTagestyp() throws FeatureRequestFailedException {
        final var featureCollection = new FeatureCollection<Feature<Tagesaggregat>>();

        var feature1 = new Feature<Tagesaggregat>();
        var tagesaggreget1 = new Tagesaggregat();
        tagesaggreget1.setAnzahlLkw(BigDecimal.valueOf(1));
        feature1.setProperties(tagesaggreget1);

        var feature2 = new Feature<Tagesaggregat>();
        var tagesaggreget2 = new Tagesaggregat();
        tagesaggreget2.setAnzahlLkw(BigDecimal.valueOf(2));
        feature2.setProperties(tagesaggreget2);

        var feature3 = new Feature<Tagesaggregat>();
        var tagesaggreget3 = new Tagesaggregat();
        tagesaggreget3.setAnzahlLkw(BigDecimal.valueOf(3));
        feature3.setProperties(tagesaggreget3);

        featureCollection.setFeatures(List.of(feature1, feature2, feature3));

        Mockito.when(arcgisRestClient.extractFeature(
                "url-tagesaggregat",
                "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-03-01 00:00:00'))",
                new ParameterizedTypeReference<FeatureCollection<Feature<Tagesaggregat>>>() {
                })).thenReturn(featureCollection);

        final var result = tagesaggregatExtractionService.getTagesaggregate(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29))))
                .toList();

        var expectedTagesaggregat1 = new TagesaggregatModel();
        expectedTagesaggregat1.setAnzahlLkw(BigDecimal.valueOf(1));
        expectedTagesaggregat1.setTagesTyp(null);
        var expectedTagesaggregat2 = new TagesaggregatModel();
        expectedTagesaggregat2.setAnzahlLkw(BigDecimal.valueOf(2));
        expectedTagesaggregat2.setTagesTyp(null);
        var expectedTagesaggregat3 = new TagesaggregatModel();
        expectedTagesaggregat3.setAnzahlLkw(BigDecimal.valueOf(3));
        expectedTagesaggregat3.setTagesTyp(null);

        final var expected = Stream.of(expectedTagesaggregat1, expectedTagesaggregat2, expectedTagesaggregat3);

        assertThat(result, containsInAnyOrder(expected.toArray()));

        Mockito.verify(arcgisRestClient, Mockito.times(1)).extractFeature(
                "url-tagesaggregat",
                "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-03-01 00:00:00'))",
                new ParameterizedTypeReference<FeatureCollection<Feature<Tagesaggregat>>>() {
                });
    }

    @Test
    void getWhereClauseForMessquerschnitteWithoutTagesTypParameter() {
        final var result = tagesaggregatExtractionService.getWhereClause(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 2))));

        final var expected = "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-02-03 00:00:00'))";

        assertThat(result, is(expected));
    }

    @Test
    void getWhereClauseForMessquerschnitteTagestypDtvw5() {
        final var result = tagesaggregatExtractionService.getWhereClause(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 2))),
                DaveTagesTyp.DTV_W5);

        final var expected = "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-02-03 00:00:00')) AND (TAGESTYP=1 OR TAGESTYP=2)";

        assertThat(result, is(expected));
    }

    @Test
    void getWhereClauseForMessquerschnitteTagestypDtvw3() {
        final var result = tagesaggregatExtractionService.getWhereClause(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29))),
                DaveTagesTyp.DTV_W3);

        final var expected = "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-03-01 00:00:00')) AND (TAGESTYP=1)";

        assertThat(result, is(expected));
    }

    @Test
    void getWhereClauseForMessquerschnitteTagesTypDtvWithoutDaveTagesTyp() {
        final var result = tagesaggregatExtractionService.getWhereClause(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 3))),
                // Bei DaveTagesTyp.DTV ist keine Filterung nach Tagestyp im where-Statement vorzunehmen
                DaveTagesTyp.DTV);

        final var expected = "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-02-04 00:00:00'))";

        assertThat(result, is(expected));
    }

    @Test
    void getWhereClauseForMessquerschnitteSameDayWithoutDaveTagesTyp() {
        final var result = tagesaggregatExtractionService.getWhereClause(
                List.of(400001, 400002, 400003, 400004),
                List.of(new ZeitraumModel(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1))),
                // Wenn StartDate == EndDate, dann ist keine Filterung nach Tagestyp im where-Statement vorzunehmen
                DaveTagesTyp.SAMSTAG);

        final var expected = "MQ_ID IN (400001,400002,400003,400004) AND ((DATUM >= TIMESTAMP '2024-02-01 00:00:00' AND DATUM < TIMESTAMP '2024-02-02 00:00:00'))";

        assertThat(result, is(expected));
    }

    @Test
    void createWhereStatementForMultipleZeitraeume() {
        final var zeitraeume = List.of(
                new ZeitraumModel(LocalDate.of(2025, 2, 8), LocalDate.of(2025, 2, 10)),
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)),
                new ZeitraumModel(LocalDate.of(2025, 2, 3), LocalDate.of(2025, 2, 5)),
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)));

        final var result = tagesaggregatExtractionService.createWhereStatementForZeitraeume(zeitraeume);

        final var expected = "((DATUM >= TIMESTAMP '2025-02-08 00:00:00' AND DATUM < TIMESTAMP '2025-02-11 00:00:00') OR (DATUM >= TIMESTAMP '2025-02-02 00:00:00' AND DATUM < TIMESTAMP '2025-02-03 00:00:00') OR (DATUM >= TIMESTAMP '2025-02-03 00:00:00' AND DATUM < TIMESTAMP '2025-02-06 00:00:00'))";

        assertThat(result, is(expected));
    }

    @Test
    void createWhereStatementForSingleZeitraeume() {
        final var zeitraeume = List.of(
                new ZeitraumModel(LocalDate.of(2025, 2, 8), LocalDate.of(2025, 2, 10)));

        final var result = tagesaggregatExtractionService.createWhereStatementForZeitraeume(zeitraeume);

        final var expected = "((DATUM >= TIMESTAMP '2025-02-08 00:00:00' AND DATUM < TIMESTAMP '2025-02-11 00:00:00'))";

        assertThat(result, is(expected));
    }

    @Test
    void areZeitraeumeTogetherCoveringOneDayWithMultipleZeitraeumeOfMultipleDays() {
        final var zeitraeume = List.of(
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)),
                new ZeitraumModel(LocalDate.of(2025, 2, 4), LocalDate.of(2025, 2, 5)),
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)));

        final var result = tagesaggregatExtractionService.areZeitraeumeTogetherCoveringOneDay(zeitraeume);

        assertThat(result, is(false));
    }

    @Test
    void areZeitraeumeTogetherCoveringOneDayWithMultipleZeitraeumeOfSingleDays() {
        final var zeitraeume = List.of(
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)),
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)),
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)));

        final var result = tagesaggregatExtractionService.areZeitraeumeTogetherCoveringOneDay(zeitraeume);

        assertThat(result, is(true));
    }

    @Test
    void areZeitraeumeTogetherCoveringOneDayWithSingleZeitraeumeOfSingleDays() {
        final var zeitraeume = List.of(
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 2)));

        final var result = tagesaggregatExtractionService.areZeitraeumeTogetherCoveringOneDay(zeitraeume);

        assertThat(result, is(true));
    }

    @Test
    void areZeitraeumeTogetherCoveringOneDayWithSingleZeitraeumeOfMultipleDays() {
        final var zeitraeume = List.of(
                new ZeitraumModel(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 3)));

        final var result = tagesaggregatExtractionService.areZeitraeumeTogetherCoveringOneDay(zeitraeume);

        assertThat(result, is(false));
    }

    @Test
    void areZeitraeumeTogetherCoveringOneDayWithNoZeitraeume() {
        final List<ZeitraumModel> zeitraeume = List.of();

        final var result = tagesaggregatExtractionService.areZeitraeumeTogetherCoveringOneDay(zeitraeume);

        assertThat(result, is(false));
    }

}
