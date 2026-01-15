package de.muenchen.dave.geodataeai.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.enums.GeometryType;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.enums.SpatialRelation;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisPoint;
import de.muenchen.dave.geodataeai.infrastructure.entity.request.geometry.ArcgisRings;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.Feature;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureCollection;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureProperties;
import de.muenchen.dave.geodataeai.infrastructure.exception.ArcgisNonRequestException;
import de.muenchen.dave.geodataeai.infrastructure.exception.ClientNonRequestException;
import de.muenchen.dave.geodataeai.infrastructure.exception.ClientRequestException;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import de.muenchen.dave.geodataeai.infrastructure.exception.GeometryNotFoundException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArcgisRestClient {

    /**
     * Referenz für die Darstellung der Koordinate in WGS84 (Identifier: EPSG 4326)
     * https://epsg.org/crs_4326/WGS-84.html
     */
    private static final String SPATIAL_REFERENCE_WGS_84 = "{ \"wkid\": 4326 }";

    private static final String GEOMETRY_PRECISION = "25";

    private final WebClient webClient;

    /**
     * Zum Ausführen des Request an den Geoserver.
     *
     * @param url zum Feature welches Abgerufen wird.
     * @param whereClause mit welcher Filterungen in der Query vorgenommen werden können.
     * @param typeRef definiert die Datenstruktur welche vom Geoserver erwartet und von dieser Methode
     *            zurückgegeben wird
     * @param <RESPONSE> als konkrete Ausprägung der erwarteten Feature-Datenstruktur.
     * @return die gefundenen Feature.
     * @throws FeatureRequestFailedException
     */
    @LogExecutionTime
    public <RESPONSE extends FeatureProperties> FeatureCollection<Feature<RESPONSE>> extractFeature(
            final String url,
            final String whereClause,
            final ParameterizedTypeReference<FeatureCollection<Feature<RESPONSE>>> typeRef) throws FeatureRequestFailedException {
        try {
            return this.getFeatureData(url, null, null, whereClause, typeRef);
        } catch (ArcgisNonRequestException | ClientRequestException | ClientNonRequestException exception) {
            final var message = "Die Ermittlung der Feature ist fehlgeschlagen.";
            final var exceptionToThrow = new FeatureRequestFailedException(message, exception);
            log.error(message, exceptionToThrow);
            throw exceptionToThrow;
        }
    }

    /**
     * Die Methode führt den Request an den Arcgis-FeatureServer aus.
     *
     * @param url zum Feature welches Abgerufen wird.
     * @param operation welche auf die Geometry und die gewünschten Feature angewendet wird
     * @param geometry mit welcher die in Parameter typeRef gegebenen Feature geschnitten werden sollen
     * @param whereClause mit welcher Filterungen in der Query vorgenommen werden können.
     * @param typeRef definiert die Datenstruktur welche von Arcgis erwartet und von dieser Methode
     *            zurückgegeben wird
     * @param <RESPONSE> als Type definiert in Parameter typeRef
     * @return die gefundenen Feature in einer Featurecollection entsprechend dem GeoJson-Standard.
     * @throws ArcgisNonRequestException
     * @throws ClientRequestException
     * @throws ClientNonRequestException
     */
    protected <RESPONSE extends FeatureProperties> FeatureCollection<Feature<RESPONSE>> getFeatureData(
            final String url,
            final SpatialRelation operation,
            final Geometry geometry,
            final String whereClause,
            final ParameterizedTypeReference<FeatureCollection<Feature<RESPONSE>>> typeRef)
            throws ArcgisNonRequestException, ClientRequestException, ClientNonRequestException {
        final var requestBody = this.createRequestBody(operation, geometry, whereClause);
        try {
            return this.webClient.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(requestBody))
                    .retrieve()
                    .onStatus(
                            httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                            clientResponse -> {
                                final var message = String.format(
                                        "Der FeatureServer hat mit Fehlerstatus %s geantwortet.",
                                        clientResponse.statusCode());
                                log.error(message);
                                return clientResponse
                                        .createException()
                                        .flatMap(webClientResponseException -> {
                                            log.error(webClientResponseException.getMessage());
                                            return Mono.error(new ClientRequestException(message, webClientResponseException));
                                        });
                            })
                    .bodyToMono(typeRef)
                    .onErrorMap(
                            Predicate.not(ClientRequestException.class::isInstance),
                            throwable -> {
                                final var message = "Es ist ein Fehler bei der Durchführung des Requests aufgetreten.";
                                log.error(message);
                                log.error(throwable.getMessage());
                                return new ClientNonRequestException(message, throwable);
                            })
                    .block();
        } catch (final Exception exception) {
            final var cause = exception.getCause();
            if (cause.getClass().equals(ClientRequestException.class)) {
                throw (ClientRequestException) cause;
            } else if (cause.getClass().equals(ClientNonRequestException.class)) {
                throw (ClientNonRequestException) cause;
            } else {
                final var message = "Im ArcgisClient ist ein allgemeiner Fehler aufgetreten.";
                log.error(message);
                throw new ClientNonRequestException(message, cause);
            }
        }
    }

    /**
     * Erstellt den Request-Body für den Request an den Arcgis-FeatureServer.
     *
     * @param operation welche auf die Geometry und die gewünschten Feature angewendet wird
     * @param geometry mit welcher die in Parameter typeRef gegebenen Feature geschnitten werden sollen
     * @param whereClause zur Filterungen in der Query.
     * @return den Body für den Request an den Arcgis-FeatureServer
     * @throws ArcgisNonRequestException
     */
    protected MultiValueMap<String, String> createRequestBody(
            final SpatialRelation operation,
            final Geometry geometry,
            final String whereClause) throws ArcgisNonRequestException {
        try {
            final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            if (StringUtils.isNotEmpty(StringUtils.trim(whereClause))) {
                requestBody.add("where", whereClause);
            }
            if (ObjectUtils.isNotEmpty(operation)) {
                requestBody.add("spatialRel", operation.getSpatialRel());
            }
            if (ObjectUtils.isNotEmpty(geometry)) {
                requestBody.add("geometry", this.createGeometryJson(geometry));
                requestBody.add(
                        "geometryType",
                        GeometryType.getGeometryTypeForGeometry(geometry).getGeometryTypeDescription());
                requestBody.add("inSR", SPATIAL_REFERENCE_WGS_84);
            }
            requestBody.add("outSR", SPATIAL_REFERENCE_WGS_84);
            requestBody.add("geometryPrecision", GEOMETRY_PRECISION);
            requestBody.add("units", "esriSRUnit_Meter");
            requestBody.add("outFields", "*");
            requestBody.add("f", "geojson");
            log.debug("Request body of Arcgis request: {}", requestBody);
            return requestBody;
        } catch (final JsonProcessingException exception) {
            final var message = "Bei der Erstellung des Request-Body ist ein Fehler aufgetreten.";
            log.error(message);
            throw new ArcgisNonRequestException(message, exception);
        } catch (final GeometryNotFoundException exception) {
            log.error(exception.getMessage());
            throw new ArcgisNonRequestException(exception.getMessage(), exception);
        }
    }

    /**
     * Erstellt eine JSON-Representation der im Parameter gegebenen Geomtry für den Request an den
     * Arcgis-FeatureServer.
     *
     * @param geometry
     * @return die JSON-Representation der Geometry.
     * @throws GeometryNotFoundException falls die Geometry nicht unterstüzt wird.
     * @throws JsonProcessingException falls keine JSON-Representation erstellt werden konnte.
     */
    protected String createGeometryJson(final Geometry geometry)
            throws GeometryNotFoundException, JsonProcessingException {
        final String geometryJson;
        if (Polygon.class.equals(geometry.getClass())) {
            final var arcgisPolygon = this.createArcgisRings((Polygon) geometry);
            geometryJson = new ObjectMapper().writeValueAsString(arcgisPolygon);
        } else if (Point.class.equals(geometry.getClass())) {
            final var arcgisPoint = this.createPoint((Point) geometry);
            geometryJson = new ObjectMapper().writeValueAsString(arcgisPoint);
        } else {
            throw new GeometryNotFoundException(
                    "Für die im Request angedachte Geometry konnte kein JSON-Objekt erzeugt werden.");
        }
        return geometryJson;
    }

    protected ArcgisRings createArcgisRings(final Polygon polygon) {
        final List<List<List<BigDecimal>>> rings = new ArrayList<>();
        final var outerLinearRing = Arrays
                .stream(polygon.getExteriorRing().getCoordinates())
                .map(this::createRingCoordinate)
                .collect(Collectors.toList());
        rings.add(outerLinearRing);
        for (int index = 0; index < polygon.getNumInteriorRing(); index++) {
            final var hole = polygon.getInteriorRingN(index);
            final var linearRingHole = Arrays
                    .stream(hole.getCoordinates())
                    .map(this::createRingCoordinate)
                    .collect(Collectors.toList());
            rings.add(linearRingHole);
        }
        final var arcgisPolygon = new ArcgisRings();
        arcgisPolygon.setRings(rings);
        return arcgisPolygon;
    }

    protected ArcgisPoint createPoint(final Point point) {
        final var arcisPoint = new ArcgisPoint();
        arcisPoint.setX(BigDecimal.valueOf(point.getX()));
        arcisPoint.setY(BigDecimal.valueOf(point.getY()));
        return arcisPoint;
    }

    protected List<BigDecimal> createRingCoordinate(final Coordinate coordinate) {
        return List.of(BigDecimal.valueOf(coordinate.getX()), BigDecimal.valueOf(coordinate.getY()));
    }
}
