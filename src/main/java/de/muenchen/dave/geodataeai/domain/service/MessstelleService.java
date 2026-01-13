/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.domain.service;

import de.muenchen.dave.geodataeai.domain.mapper.FeatureResponseDomainMapper;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureCollectionModel;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessfaehigkeitModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessquerschnittModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessstelleModel;
import de.muenchen.dave.geodataeai.infrastructure.client.ArcgisRestClient;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.Feature;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureCollection;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messfaehigkeit;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messquerschnitt;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messstelle;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessstelleService {

    /**
     * Extrahieren aller Messstellen deren ID nicht mit 1 beginnt.
     * <p>
     * "SUBSTRING(CAST(\"MST_ID\" AS CHARACTER(8)), 0, 1) <> '1'"
     */
    public static final String WHERE_CLAUSE_MESSSTELLEN = "SUBSTRING(CAST(\""
            .concat(Messstelle.MST_ID)
            .concat("\" AS CHARACTER(8)), 0, 1) <> '1'");

    private final ArcgisRestClient arcgisRestClient;

    private final FeatureResponseDomainMapper featureResponseDomainMapper;

    private final String urlMessstelle;

    private final String urlMessquerschnitt;

    private final String urlMessfaehigkeit;

    public MessstelleService(final ArcgisRestClient arcgisRestClient,
            final FeatureResponseDomainMapper featureResponseDomainMapper,
            @Value("${feature.server.url.messstelle}") final String urlMessstelle,
            @Value("${feature.server.url.messquerschnitt}") final String urlMessquerschnitt,
            @Value("${feature.server.url.messfaehigkeit}") final String urlMessfaehigkeit) {
        this.arcgisRestClient = arcgisRestClient;
        this.featureResponseDomainMapper = featureResponseDomainMapper;
        this.urlMessstelle = urlMessstelle;
        this.urlMessquerschnitt = urlMessquerschnitt;
        this.urlMessfaehigkeit = urlMessfaehigkeit;
    }

    /**
     * Ermittelt alle relevanten Messstellen und fügt den Messstellen die entsprechenden
     * Messquerschnitte und Messfaehigkeiten an.
     *
     * @return die relevanten Messstellen bei welchen die ID nicht mit einer 1 beginnt.
     */
    public FeatureCollectionModel<FeatureModel<MessstelleModel>> getMessstellen() throws FeatureRequestFailedException {

        // Messstellen sowie Messquerschnitte und Messfähigkeiten zu den Messstellen holen.
        final var messstellenEntity = arcgisRestClient.extractFeature(
                this.urlMessstelle,
                WHERE_CLAUSE_MESSSTELLEN,
                new ParameterizedTypeReference<FeatureCollection<Feature<Messstelle>>>() {
                });
        final var commaSeparatedMessstellenIds = getCommaSeperatedMessstellenIds(messstellenEntity);

        final var messquerschnitteEntity = arcgisRestClient.extractFeature(
                this.urlMessquerschnitt,
                this.getWhereClauseMessquerschnitt(commaSeparatedMessstellenIds),
                new ParameterizedTypeReference<FeatureCollection<Feature<Messquerschnitt>>>() {
                });

        final var messfaehigkeitenEntity = arcgisRestClient.extractFeature(
                this.urlMessfaehigkeit,
                this.getWhereClauseMessfaehigkeit(commaSeparatedMessstellenIds),
                new ParameterizedTypeReference<FeatureCollection<Feature<Messfaehigkeit>>>() {
                });

        // Modelmapping und Gruppieren der Messquerschnitte und Messfähigkeiten nach Messstellenids
        final var messquerschnitteModelGroupedByMessstelleIds = featureResponseDomainMapper.messquerschnittFeatureCollectionEntity2Model(messquerschnitteEntity)
                .getFeatures()
                .stream()
                .collect(Collectors.groupingBy(messquerschnittFeature -> messquerschnittFeature.getProperties().getMstId()));

        final var messfaehigkeitenModelGroupedByMessstelleIds = featureResponseDomainMapper.messfaehigkeitFeatureCollectionEntity2Model(messfaehigkeitenEntity)
                .getFeatures()
                .stream()
                .filter(messfaehigkeitFeature -> Objects.nonNull(messfaehigkeitFeature.getProperties().getMstId()))
                .collect(Collectors.groupingBy(messfaehigkeitFeature -> messfaehigkeitFeature.getProperties().getMstId()));

        // Anfügen der Messquerschnitte
        final var messstellenModel = featureResponseDomainMapper.messstelleFeatureCollectionEntity2Model(messstellenEntity);

        messstellenModel.getFeatures()
                .stream()
                .forEach(messstelleFeature -> {
                    final var messstelleId = messstelleFeature.getProperties().getMstId();
                    final var featureCollectionMessquerschnitte = new FeatureCollectionModel<FeatureModel<MessquerschnittModel>>();
                    featureCollectionMessquerschnitte.setFeatures(
                            messquerschnitteModelGroupedByMessstelleIds.get(messstelleId));
                    messstelleFeature.getProperties()
                            .setMessquerschnitte(featureCollectionMessquerschnitte);
                    final var featureCollectionMessfaehigkeiten = new FeatureCollectionModel<FeatureModel<MessfaehigkeitModel>>();
                    featureCollectionMessfaehigkeiten.setFeatures(
                            messfaehigkeitenModelGroupedByMessstelleIds.get(messstelleId));
                    messstelleFeature.getProperties()
                            .setMessfaehigkeiten(featureCollectionMessfaehigkeiten);
                });

        return messstellenModel;
    }

    /**
     * Ermittelt aus den übergebenen Messstellen die IDs.
     * <p>
     * "4016,...,4020"
     *
     * @param messstellen
     * @return die mit einem Komma separierten Messstellen-IDs.
     */
    protected String getCommaSeperatedMessstellenIds(final FeatureCollection<Feature<Messstelle>> messstellen) {
        return CollectionUtils.emptyIfNull(messstellen.getFeatures())
                .stream()
                .map(Feature::getProperties)
                .map(Messstelle::getMstId)
                .filter(ObjectUtils::isNotEmpty)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    /**
     * Erstellt die where-Clause der Arcgis-Abfrage um Messquerschnitte auf Basis der Messtellen-IDs
     * abzurufen.
     * <p>
     * MST_ID IN (4016,...,4020)
     *
     * @param commaSeparatedMessstellenIds
     * @return die where-Clause.
     */
    protected String getWhereClauseMessquerschnitt(final String commaSeparatedMessstellenIds) {
        return Messstelle.MST_ID
                .concat(" IN (")
                .concat(commaSeparatedMessstellenIds)
                .concat(")");
    }

    /**
     * Erstellt die where-Clause der Arcgis-Abfrage um Messfaehigkeiten auf Basis der Messtellen-IDs
     * abzurufen.
     * <p>
     * MST_ID IN (4016,...,4020)
     *
     * @param commaSeparatedMessstellenIds
     * @return die where-Clause.
     */
    protected String getWhereClauseMessfaehigkeit(final String commaSeparatedMessstellenIds) {
        return Messstelle.MST_ID
                .concat(" IN (")
                .concat(commaSeparatedMessstellenIds)
                .concat(")");
    }

}
