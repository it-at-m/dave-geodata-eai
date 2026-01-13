package de.muenchen.dave.geodataeai.infrastructure.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.enums.SpatialRelation;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisPoint;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisRings;
import de.muenchen.dave.geodataeai.infrastructure.exception.ArcgisNonRequestException;
import de.muenchen.dave.geodataeai.infrastructure.exception.GeometryNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArcgisRestClientTest {

    @Mock
    private WebClient webClient;

    private ArcgisRestClient arcgisRestClient;

    @BeforeEach
    public void beforeEach() {
        this.arcgisRestClient = new ArcgisRestClient(webClient);
        Mockito.reset(webClient);
    }

    @Test
    void createRequestBodyAllParameterNull() throws ArcgisNonRequestException {
        var result = arcgisRestClient.createRequestBody(null, null, null);

        final var expected = new LinkedMultiValueMap<String, String>();
        expected.add("outSR", "{ \"wkid\": 4326 }");
        expected.add("geometryPrecision", "25");
        expected.add("units", "esriSRUnit_Meter");
        expected.add("outFields", "*");
        expected.add("f", "geojson");

        assertThat(result, is(expected));

        result = arcgisRestClient.createRequestBody(null, null, "       ");

        assertThat(result, is(expected));
    }

    @Test
    void createRequestBodyWithWhereClause() throws ArcgisNonRequestException {
        var result = arcgisRestClient.createRequestBody(null, null, "  xxx x xx   ");

        final var expected = new LinkedMultiValueMap<String, String>();
        expected.add("where", "  xxx x xx   ");
        expected.add("outSR", "{ \"wkid\": 4326 }");
        expected.add("geometryPrecision", "25");
        expected.add("units", "esriSRUnit_Meter");
        expected.add("outFields", "*");
        expected.add("f", "geojson");

        assertThat(result, is(expected));
    }

    @Test
    void createRequestBodyWithSpatialRelation() throws ArcgisNonRequestException {
        var result = arcgisRestClient.createRequestBody(SpatialRelation.INTERSECTS, null, null);

        final var expected = new LinkedMultiValueMap<String, String>();
        expected.add("spatialRel", "esriSpatialRelIntersects");
        expected.add("outSR", "{ \"wkid\": 4326 }");
        expected.add("geometryPrecision", "25");
        expected.add("units", "esriSRUnit_Meter");
        expected.add("outFields", "*");
        expected.add("f", "geojson");

        assertThat(result, is(expected));
    }

    @Test
    void createRequestBodyValidWithGeometry() throws ArcgisNonRequestException {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate = new Coordinate();
        coordinate.setX(1.111);
        coordinate.setY(2.222);
        final var point = geometryFactory.createPoint(coordinate);

        var result = arcgisRestClient.createRequestBody(null, point, null);

        final var expected = new LinkedMultiValueMap<String, String>();
        expected.add("geometry", "{\"x\":1.111,\"y\":2.222}");
        expected.add("geometryType", "esriGeometryPoint");
        expected.add("inSR", "{ \"wkid\": 4326 }");
        expected.add("outSR", "{ \"wkid\": 4326 }");
        expected.add("geometryPrecision", "25");
        expected.add("units", "esriSRUnit_Meter");
        expected.add("outFields", "*");
        expected.add("f", "geojson");

        assertThat(result, is(expected));
    }

    @Test
    void createRequestBodyInvalidWithGeometry() {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate = new Coordinate();
        coordinate.setX(1.111);
        coordinate.setY(2.222);
        final var multiPolygon = geometryFactory.createMultiPolygon();

        Assertions.assertThrows(
                ArcgisNonRequestException.class,
                () -> this.arcgisRestClient.createRequestBody(null, multiPolygon, null));

    }

    @Test
    void createGeometryJsonGeometryNotFoundException() {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var multiPolygon = geometryFactory.createMultiPolygon();

        Assertions.assertThrows(
                GeometryNotFoundException.class,
                () -> this.arcgisRestClient.createGeometryJson(multiPolygon));
    }

    @Test
    void createGeometryJsonOfPolygon() throws GeometryNotFoundException, JsonProcessingException {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate1 = new Coordinate();
        coordinate1.setX(1.111);
        coordinate1.setY(1.111);
        final var coordinate2 = new Coordinate();
        coordinate2.setX(2.222);
        coordinate2.setY(1.111);
        final var coordinate3 = new Coordinate();
        coordinate3.setX(2.222);
        coordinate3.setY(2.222);
        final var coordinate4 = new Coordinate();
        coordinate4.setX(1.111);
        coordinate4.setY(2.222);
        final var coordinate5 = new Coordinate();
        coordinate5.setX(1.111);
        coordinate5.setY(1.111);
        final var linearRing = geometryFactory
                .createLinearRing(
                        List.of(coordinate1, coordinate2, coordinate3, coordinate4, coordinate5)
                                .toArray(new Coordinate[5]));
        final var polygon = geometryFactory.createPolygon(linearRing);

        final var result = arcgisRestClient.createGeometryJson(polygon);

        final var expected = "{\"rings\":[[[1.111,1.111],[2.222,1.111],[2.222,2.222],[1.111,2.222],[1.111,1.111]]]}";

        assertThat(result, is(expected));
    }

    @Test
    void createGeometryJsonOfPoint() throws GeometryNotFoundException, JsonProcessingException {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate = new Coordinate();
        coordinate.setX(1.111);
        coordinate.setY(2.222);
        final var point = geometryFactory.createPoint(coordinate);

        final var result = arcgisRestClient.createGeometryJson(point);

        final var expected = "{\"x\":1.111,\"y\":2.222}";

        assertThat(result, is(expected));
    }

    @Test
    void createArcgisRings() {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate1 = new Coordinate();
        coordinate1.setX(1.111);
        coordinate1.setY(1.111);
        final var coordinate2 = new Coordinate();
        coordinate2.setX(2.222);
        coordinate2.setY(1.111);
        final var coordinate3 = new Coordinate();
        coordinate3.setX(2.222);
        coordinate3.setY(2.222);
        final var coordinate4 = new Coordinate();
        coordinate4.setX(1.111);
        coordinate4.setY(2.222);
        final var coordinate5 = new Coordinate();
        coordinate5.setX(1.111);
        coordinate5.setY(1.111);
        final var linearRing = geometryFactory
                .createLinearRing(
                        List.of(coordinate1, coordinate2, coordinate3, coordinate4, coordinate5)
                                .toArray(new Coordinate[5]));
        final var polygon = geometryFactory.createPolygon(linearRing);

        final var result = arcgisRestClient.createArcgisRings(polygon);

        final var expected = new ArcgisRings();
        expected.setRings(List.of(
                List.of(
                        List.of(BigDecimal.valueOf(1.111), BigDecimal.valueOf(1.111)),
                        List.of(BigDecimal.valueOf(2.222), BigDecimal.valueOf(1.111)),
                        List.of(BigDecimal.valueOf(2.222), BigDecimal.valueOf(2.222)),
                        List.of(BigDecimal.valueOf(1.111), BigDecimal.valueOf(2.222)),
                        List.of(BigDecimal.valueOf(1.111), BigDecimal.valueOf(1.111)))));

        assertThat(result, is(expected));
    }

    @Test
    void createPoint() {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate = new Coordinate();
        coordinate.setX(1.111);
        coordinate.setY(2.222);
        final var point = geometryFactory.createPoint(coordinate);

        final var result = arcgisRestClient.createPoint(point);

        final var expected = new ArcgisPoint();
        expected.setX(BigDecimal.valueOf(1.111));
        expected.setY(BigDecimal.valueOf(2.222));

        assertThat(result, is(expected));
    }

    @Test
    void createRingCoordinate() {
        final var coordinate = new Coordinate();
        coordinate.setX(1.111);
        coordinate.setY(2.222);

        final var result = arcgisRestClient.createRingCoordinate(coordinate);

        final var expected = List.of(BigDecimal.valueOf(1.111), BigDecimal.valueOf(2.222));

        assertThat(result, is(expected));
    }

}
