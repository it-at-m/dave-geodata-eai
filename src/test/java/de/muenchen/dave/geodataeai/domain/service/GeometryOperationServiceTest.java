package de.muenchen.dave.geodataeai.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.exception.GeometryOperationFailedException;
import de.muenchen.dave.geodataeai.domain.model.geometry.GeometryModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.PointGeometryModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisPoint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GeometryOperationServiceTest {

    private final GeometryOperationService geometryOperationService = new GeometryOperationService();

    @Test
    void createPoint() {
        final PointGeometryModel pointGeometry = new PointGeometryModel();
        pointGeometry.setType("Point");
        pointGeometry.setCoordinates(
                List.of(BigDecimal.valueOf(11.543319557691385), BigDecimal.valueOf(48.10903906645363)));

        final Point point = this.geometryOperationService.createPoint(pointGeometry);

        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final Point expected = geometryFactory.createPoint(new Coordinate(11.543319557691385, 48.10903906645363));

        assertThat(point, is(expected));
    }

    @Test
    void createGeometryModel() throws GeometryOperationFailedException {
        final var arcgisPoint = new ArcgisPoint();
        arcgisPoint.setX(BigDecimal.valueOf(11.543319557691385));
        arcgisPoint.setY(BigDecimal.valueOf(48.10903906645363));

        GeometryModel result = this.geometryOperationService.createGeometryModel(arcgisPoint);

        final PointGeometryModel expected1 = new PointGeometryModel();
        expected1.setType("Point");
        expected1.setCoordinates(
                List.of(BigDecimal.valueOf(11.543319557691385), BigDecimal.valueOf(48.10903906645363)));

        assertThat(result, is(expected1));
    }

    @Test
    void createPointGeometryModel() throws GeometryOperationFailedException {
        final var arcgisPoint = new ArcgisPoint();
        arcgisPoint.setX(BigDecimal.valueOf(11.543319557691385));
        arcgisPoint.setY(BigDecimal.valueOf(48.10903906645363));

        PointGeometryModel result = this.geometryOperationService.createPointGeometryModel(arcgisPoint);

        final PointGeometryModel expected1 = new PointGeometryModel();
        expected1.setType("Point");
        expected1.setCoordinates(
                List.of(BigDecimal.valueOf(11.543319557691385), BigDecimal.valueOf(48.10903906645363)));

        assertThat(result, is(expected1));
    }

    @Test
    void createCoordinate() {
        final var result = this.geometryOperationService.createCoordinate(
                List.of(BigDecimal.valueOf(11.543319557691385), BigDecimal.valueOf(48.10903906645363)));

        final var expected = new Coordinate(11.543319557691385, 48.10903906645363);

        assertThat(expected, Matchers.is(result));

        Assertions.assertThrows(
                GeometryOperationFailedException.class,
                () -> this.geometryOperationService.createCoordinate(new ArrayList<>()));
    }
}
