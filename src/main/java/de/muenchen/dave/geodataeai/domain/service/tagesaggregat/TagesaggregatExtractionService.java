package de.muenchen.dave.geodataeai.domain.service.tagesaggregat;

import de.muenchen.dave.geodataeai.domain.mapper.FeatureResponseDomainMapper;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.ZeitraumModel;
import de.muenchen.dave.geodataeai.infrastructure.client.ArcgisRestClient;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MobidamFeatureTagesTyp;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.Feature;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureCollection;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messquerschnitt;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messwerte.Tagesaggregat;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TagesaggregatExtractionService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ArcgisRestClient arcgisRestClient;

    private final String urlMesswerteTagesaggregat;

    private final FeatureResponseDomainMapper responseDomainMapper;

    public TagesaggregatExtractionService(
            final ArcgisRestClient arcgisRestClient,
            final FeatureResponseDomainMapper responseDomainMapper,
            @Value("${feature.server.url.messwerte-tagesaggregat}") final String urlMesswerteTagesaggregat) {
        this.arcgisRestClient = arcgisRestClient;
        this.responseDomainMapper = responseDomainMapper;
        this.urlMesswerteTagesaggregat = urlMesswerteTagesaggregat;
    }

    /**
     * Extrahiert die Tagesaggregate auf Basis der gegebenen Methodenparameter aus Mobidam.
     *
     * @param messquerschnittIds für die Messquerschnitte
     * @param zeitraeume der Perioden mit dem jeweiligen Startdatum und Enddatum der Periode
     * @param tagesTyp
     * @return die Tagesaggregate
     * @throws FeatureRequestFailedException
     */
    public Stream<TagesaggregatModel> getTagesaggregate(
            final List<Integer> messquerschnittIds,
            final List<ZeitraumModel> zeitraeume,
            final DaveTagesTyp tagesTyp) throws FeatureRequestFailedException {
        final var tagesaggregate = arcgisRestClient.extractFeature(
                urlMesswerteTagesaggregat,
                this.getWhereClause(messquerschnittIds, zeitraeume, tagesTyp),
                new ParameterizedTypeReference<FeatureCollection<Feature<Tagesaggregat>>>() {
                });
        return CollectionUtils.emptyIfNull(tagesaggregate
                .getFeatures())
                .stream()
                .map(Feature::getProperties)
                .map(tagesaggregat -> responseDomainMapper.tagesaggregatEntity2Model(tagesaggregat, tagesTyp));

    }

    /**
     * Extrahiert die Tagesaggregate auf Basis der gegebenen Methodenparameter aus Mobidam.
     *
     * @param messquerschnittIds für die Messquerschnitte
     * @param zeitraeume der Perioden mit dem jeweiligen Startdatum und Enddatum der Periode
     * @return die Tagesaggregate
     * @throws FeatureRequestFailedException
     */
    public Stream<TagesaggregatModel> getTagesaggregate(
            final Collection<Integer> messquerschnittIds,
            final List<ZeitraumModel> zeitraeume) throws FeatureRequestFailedException {
        final var tagesaggregate = arcgisRestClient.extractFeature(
                urlMesswerteTagesaggregat,
                this.getWhereClause(messquerschnittIds, zeitraeume),
                new ParameterizedTypeReference<FeatureCollection<Feature<Tagesaggregat>>>() {
                });
        return CollectionUtils.emptyIfNull(tagesaggregate
                .getFeatures())
                .parallelStream()
                .map(Feature::getProperties)
                .map(tagesaggregat -> responseDomainMapper.tagesaggregatEntity2Model(tagesaggregat, null));
    }

    /**
     * Die Methode erstellt das Where-Statement zur Extraktion der Tagesaggregate
     * basierend auf die gegebene Periode und den Messquerschnitt-IDs.
     *
     * @param messquerschnittIds für die Messquerschnitte
     * @param zeitraeume der Perioden mit dem jeweiligen Startdatum und Enddatum der Periode
     * @param tagesTyp
     * @return die Where-Clause.
     */
    protected String getWhereClause(
            final List<Integer> messquerschnittIds,
            final List<ZeitraumModel> zeitraeume,
            final DaveTagesTyp tagesTyp) {
        return getWhereClause(messquerschnittIds, zeitraeume)
                /**
                 * Bei der Anfrage eines einzigen Tages ist der Tagestyp nicht relevant.
                 * Des Weiteren ist ebenfalls kein Tagestyp erforderlich wenn {@link DaveTagesTyp.DTV} gesetzt ist.
                 */
                .concat(areZeitraeumeTogetherCoveringOneDay(zeitraeume) || tagesTyp.getMobidamFeatureTagesTyp().isEmpty() ? ""
                        : createWhereStatementTagestyp(tagesTyp.getMobidamFeatureTagesTyp()));
    }

    /**
     * Die Methode erstellt das Where-Statement zur Extraktion der Tagesaggregate
     * basierend auf die gegebene Periode und den Messquerschnitt-IDs.
     *
     * @param messquerschnittIds für die Messquerschnitte
     * @param zeitraeume der Perioden mit dem jeweiligen Startdatum und Enddatum der Periode
     * @return die Where-Clause.
     */
    protected String getWhereClause(
            final Collection<Integer> messquerschnittIds,
            final List<ZeitraumModel> zeitraeume) {
        final var commaSeperatedMessquerschnittIds = messquerschnittIds.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "(", ")"));

        final var whereClauseMqId = Messquerschnitt.MQ_ID
                .concat(" IN ")
                .concat(commaSeperatedMessquerschnittIds);

        final var whereClauseZeitraeume = createWhereStatementForZeitraeume(zeitraeume);

        return whereClauseMqId
                .concat(" AND ")
                .concat(whereClauseZeitraeume);
    }

    protected String createWhereStatementForZeitraeume(final List<ZeitraumModel> zeitraeume) {
        return zeitraeume.stream()
                .distinct()
                .map(zeitraum -> createWhereStatementForPeriod(zeitraum.getStartDate(), zeitraum.getEndDate()))
                .collect(Collectors.joining(" OR ", "(", ")"));
    }

    protected String createWhereStatementForPeriod(final LocalDate startDate, final LocalDate endDate) {
        return "("
                .concat(this.createWhereStatementDatumUhrzeitLowerBound(startDate))
                .concat(" AND ")
                .concat(this.createWhereStatementDatumUhrzeitUpperBound(endDate))
                .concat(")");
    }

    protected String createWhereStatementDatumUhrzeitLowerBound(final LocalDate date) {
        return Tagesaggregat.DATUM
                .concat(" >= TIMESTAMP '")
                .concat(DATE_FORMATTER.format(date))
                .concat(" ")
                .concat(TIME_FORMATTER.format(LocalTime.MIDNIGHT))
                .concat("'");
    }

    protected String createWhereStatementDatumUhrzeitUpperBound(final LocalDate date) {
        return Tagesaggregat.DATUM
                .concat(" < TIMESTAMP '")
                .concat(DATE_FORMATTER.format(date.plusDays(1)))
                .concat(" ")
                .concat(TIME_FORMATTER.format(LocalTime.MIDNIGHT))
                .concat("'");
    }

    protected String createWhereStatementTagestyp(final List<MobidamFeatureTagesTyp> tagesTypen) {
        final var whereClause = " AND ";

        final var tagesTypStatements = tagesTypen.stream()
                .map(tagesTyp -> Tagesaggregat.TAGESTYP.concat("=").concat(tagesTyp.getTyp().toString()))
                .collect(Collectors.joining(" OR ", "(", ")"));

        return whereClause.concat(tagesTypStatements);
    }

    protected boolean areZeitraeumeTogetherCoveringOneDay(final List<ZeitraumModel> zeitraeume) {
        return !zeitraeume.isEmpty() &&
                zeitraeume.getFirst().getStartDate().isEqual(zeitraeume.getFirst().getEndDate()) &&
                zeitraeume.stream().distinct().count() <= 1;
    }

}
