/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.api.mapper;

import de.muenchen.dave.geodataeai.api.dto.messstelle.MessfaehigkeitDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.MessquerschnittDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.MessstelleDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.UnauffaelligerTagDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.IntervalResponseDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.TagesaggregatResponseDto;
import de.muenchen.dave.geodataeai.configuration.MapstructConfiguration;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.PointGeometryModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessfaehigkeitModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessquerschnittModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessstelleModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.UnauffaelligerTagModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalResponseModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatResponseModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Verkehrsart;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface ResponseApiMapper {

    default MessstelleDto featureMessstelleModel2Dto(final FeatureModel<MessstelleModel> model) {
        final MessstelleDto messstelleDto = messstelleModel2Dto(model.getProperties());
        if (model.getGeometry().getType().equalsIgnoreCase("Point")) {
            final List<BigDecimal> coordinates = ((PointGeometryModel) model.getGeometry()).getCoordinates();
            messstelleDto.setLongitude(coordinates.getFirst().doubleValue());
            messstelleDto.setLatitude(coordinates.getLast().doubleValue());
        }
        return messstelleDto;
    }

    default MessquerschnittDto featureMessquerschnittModel2Dto(final FeatureModel<MessquerschnittModel> model) {
        final MessquerschnittDto messquerschnittDto = messquerschnittModel2Dto(model.getProperties());
        if (model.getGeometry().getType().equalsIgnoreCase("Point")) {
            final List<BigDecimal> coordinates = ((PointGeometryModel) model.getGeometry()).getCoordinates();
            messquerschnittDto.setLongitude(coordinates.getFirst().doubleValue());
            messquerschnittDto.setLatitude(coordinates.getLast().doubleValue());
        }
        return messquerschnittDto;
    }

    @Mapping(target = "fahrzeugklasse", ignore = true)
    @Mapping(target = "detektierteVerkehrsart", ignore = true)
    @Mapping(target = "hersteller", ignore = true)
    @Mapping(target = "messquerschnitte", ignore = true)
    @Mapping(target = "messfaehigkeiten", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    MessstelleDto messstelleModel2Dto(final MessstelleModel model);

    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "lageMessquerschnitt", source = "beschreibung")
    MessquerschnittDto messquerschnittModel2Dto(final MessquerschnittModel model);

    MessfaehigkeitDto messfaehigkeitModel2Dto(final MessfaehigkeitModel model);

    @AfterMapping
    default void messstelleModel2DtoAfterMapping(@MappingTarget MessstelleDto dto, final MessstelleModel model) {
        dto.setDetektierteVerkehrsart(Verkehrsart.UNBEKANNT);
        if (ObjectUtils.isNotEmpty(model.getMessquerschnitte()) && CollectionUtils.isNotEmpty(model.getMessquerschnitte().getFeatures())) {
            final Set<Verkehrsart> verkehrsart = new HashSet<>();
            dto.setMessquerschnitte(model.getMessquerschnitte().getFeatures().stream().map(messquerschnittModelFeatureModel -> {
                final var messquerschnittModel = messquerschnittModelFeatureModel.getProperties();
                verkehrsart.add(messquerschnittModel.getDetektierteVerkehrsart());
                if (StringUtils.isNotEmpty(messquerschnittModel.getHersteller())) {
                    dto.setHersteller(messquerschnittModel.getHersteller());
                }
                return featureMessquerschnittModel2Dto(messquerschnittModelFeatureModel);
            }).collect(Collectors.toList()));

            final var nonNullVerkehrsarten = verkehrsart.stream().filter(Objects::nonNull).toList();
            if (nonNullVerkehrsarten.size() == 1) {
                dto.setDetektierteVerkehrsart(nonNullVerkehrsarten.getFirst());
            }
        }

        if (ObjectUtils.isNotEmpty(model.getMessfaehigkeiten()) && CollectionUtils.isNotEmpty(model.getMessfaehigkeiten().getFeatures())) {
            dto.setMessfaehigkeiten(model.getMessfaehigkeiten().getFeatures().stream()
                    .map(messfaehigkeitModelFeatureModel -> messfaehigkeitModel2Dto(messfaehigkeitModelFeatureModel.getProperties())).collect(
                            Collectors.toList()));
            dto.getMessfaehigkeiten().stream().max(Comparator.comparing(MessfaehigkeitDto::getGueltigBis))
                    .ifPresent(messfaehigkeitDto -> dto.setFahrzeugklasse(messfaehigkeitDto.getFahrzeugklasse()));
        }
    }

    IntervalResponseDto model2Dto(final IntervalResponseModel model);

    TagesaggregatResponseDto model2Dto(final TagesaggregatResponseModel model);

    List<UnauffaelligerTagDto> model2Dto(final List<UnauffaelligerTagModel> models);
}
