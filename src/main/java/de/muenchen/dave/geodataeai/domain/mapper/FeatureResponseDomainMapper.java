/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.domain.mapper;

import de.muenchen.dave.geodataeai.configuration.MapstructConfiguration;
import de.muenchen.dave.geodataeai.domain.model.enums.DaveTagesTyp;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureCollectionModel;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.GeometryModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.MultiPolygonGeometryModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.PointGeometryModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.PolygonGeometryModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessfaehigkeitModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessquerschnittModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessstelleModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.UnauffaelligerTagModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.Feature;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.feature.FeatureCollection;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry.Geometry;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry.MultiPolygonGeometry;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry.PointGeometry;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.geometry.PolygonGeometry;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messfaehigkeit;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messquerschnitt;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messstelle.Messstelle;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.messwerte.Tagesaggregat;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;

@Slf4j
@Mapper(config = MapstructConfiguration.class)
public abstract class FeatureResponseDomainMapper {

    @Mapping(target = "messquerschnitte", ignore = true)
    @Mapping(target = "messfaehigkeiten", ignore = true)
    @Mapping(
            source = "stadtbezirkNummer",
            target = "stadtbezirkNummer",
            defaultExpression = "java( 999 )"
    )
    public abstract MessstelleModel messstelleEntity2Model(final Messstelle entity);

    public abstract FeatureCollectionModel<FeatureModel<MessstelleModel>> messstelleFeatureCollectionEntity2Model(
            final FeatureCollection<Feature<Messstelle>> entities);

    public abstract FeatureCollectionModel<FeatureModel<MessquerschnittModel>> messquerschnittFeatureCollectionEntity2Model(
            final FeatureCollection<Feature<Messquerschnitt>> entities);

    public abstract FeatureCollectionModel<FeatureModel<MessfaehigkeitModel>> messfaehigkeitFeatureCollectionEntity2Model(
            final FeatureCollection<Feature<Messfaehigkeit>> entities);

    @SubclassMapping(source = PointGeometry.class, target = PointGeometryModel.class)
    @SubclassMapping(source = PolygonGeometry.class, target = PolygonGeometryModel.class)
    @SubclassMapping(source = MultiPolygonGeometry.class, target = MultiPolygonGeometryModel.class)
    public abstract GeometryModel entity2Model(final Geometry entity);

    @Mapping(target = "tagesTyp", ignore = true)
    @Mapping(target = "includedMeasuringDays", ignore = true)
    public abstract TagesaggregatModel tagesaggregatEntity2Model(final Tagesaggregat entity, @Context final DaveTagesTyp daveTagesTyp);

    @AfterMapping
    public void tagesaggregatEntity2ModelAfterMapping(
            @MappingTarget final TagesaggregatModel model,
            @Context final DaveTagesTyp daveTagesTyp) {
        model.setTagesTyp(daveTagesTyp);
    }

    @Mapping(target = "mstId", ignore = true)
    public abstract UnauffaelligerTagModel tagesaggregat2UnauffaelligerTag(
            final TagesaggregatModel model,
            @Context final Map<Integer, Integer> mstIdByMqId);

    @AfterMapping
    public void tagesaggregat2UnauffaelligerTagAfterMapping(
            final TagesaggregatModel tagesaggregat,
            @MappingTarget final UnauffaelligerTagModel unauffaelligerTag,
            @Context final Map<Integer, Integer> mstIdByMqId) {
        final var mstId = mstIdByMqId.get(tagesaggregat.getMqId());
        unauffaelligerTag.setMstId(Objects.isNull(mstId) ? null : String.valueOf(mstId));
    }
}
