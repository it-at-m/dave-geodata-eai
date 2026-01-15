package de.muenchen.dave.geodataeai.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.dave.geodataeai.domain.exception.GeometryOperationFailedException;
import de.muenchen.dave.geodataeai.domain.model.geometry.GeometryModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.PointGeometryModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisGeometry;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisPoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeometryOperationService {

    private static final int NUMBER_GEO_JSON_DECIMALS = 25;

    public Point createPoint(final PointGeometryModel pointGeometry) {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate = createCoordinate(pointGeometry.getCoordinates());
        return geometryFactory.createPoint(coordinate);
    }

    /**
     * Die Methode erwartet eine ArcgisGeometry und erstellt daraus eine Geometry.
     *
     * @param arcgisGeometry als {@link ArcgisPoint}
     * @return die Geometry
     * @throws GeometryOperationFailedException falls es sich bei der Geometry im Parameter nicht um
     *             einen {@link ArcgisPoint} handelt.
     */
    public GeometryModel createGeometryModel(final ArcgisGeometry arcgisGeometry)
            throws GeometryOperationFailedException {
        final GeometryModel geometryModel;
        if (ArcgisPoint.class.equals(arcgisGeometry.getClass())) {
            geometryModel = this.createPointGeometryModel((ArcgisPoint) arcgisGeometry);
        } else {
            final var message = "Die Geometry " +
                    arcgisGeometry.getClass().getCanonicalName() +
                    " der Feature-Response ist nicht in der EAI definiert.";
            final var exceptionToThrow = new GeometryOperationFailedException(message);
            log.error(message, exceptionToThrow);
            throw exceptionToThrow;
        }
        return geometryModel;
    }

    public PointGeometryModel createPointGeometryModel(final ArcgisPoint point)
            throws GeometryOperationFailedException {
        final var geometryFactory = JTSFactoryFinder.getGeometryFactory();
        final var coordinate = new Coordinate(point.getX().doubleValue(), point.getY().doubleValue());
        final var pointCoordiante = geometryFactory.createPoint(coordinate);
        final GeometryJSON jsonGeometry = new GeometryJSON(NUMBER_GEO_JSON_DECIMALS);
        final ObjectMapper objectMapper = new ObjectMapper();
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            jsonGeometry.writePoint(pointCoordiante, outputStream);
            return objectMapper.readValue(outputStream.toByteArray(), PointGeometryModel.class);
        } catch (final IOException exception) {
            final var message = "Das übergebene Multipolygon konnte nicht verarbeitet werden.";
            log.error(message);
            throw new GeometryOperationFailedException(message, exception);
        }
    }

    @SneakyThrows
    public Coordinate createCoordinate(final List<BigDecimal> coordinate) {
        if (coordinate.size() == 2) {
            return new Coordinate(coordinate.get(0).doubleValue(), coordinate.get(1).doubleValue());
        } else {
            final var message = "Die Punktkoordinate repräsentiert durch eine Liste stellt keine Punktkoordinate dar.";
            final var exceptionToThrow = new GeometryOperationFailedException(message);
            log.error(message, exceptionToThrow);
            throw exceptionToThrow;
        }
    }
}
