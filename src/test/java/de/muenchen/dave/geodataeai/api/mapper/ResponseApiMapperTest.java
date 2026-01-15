package de.muenchen.dave.geodataeai.api.mapper;

import de.muenchen.dave.geodataeai.api.dto.messstelle.MessfaehigkeitDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.MessquerschnittDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.MessstelleDto;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureCollectionModel;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.geometry.PointGeometryModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessfaehigkeitModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessquerschnittModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessstelleModel;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Fahrzeugklasse;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.MessstelleStatus;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.Verkehrsart;
import de.muenchen.dave.geodataeai.infrastructure.entity.response.enums.ZaehldatenIntervall;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ResponseApiMapperTest {

    private final ResponseApiMapper mapperToTest = new ResponseApiMapperImpl();

    @Test
    void messquerschnittModel2Dto() {
        final var model = new MessquerschnittModel();
        model.setMqId(12345);
        model.setMstId(123);
        model.setStrassenname("setStrassenname");
        model.setBeschreibung("setBeschreibung");
        model.setFahrtrichtung("S");
        model.setAnzahlFahrspuren(1);
        model.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        model.setHersteller("setHersteller");
        model.setAnzahlDetektoren(2);

        final var expected = new MessquerschnittDto();
        expected.setMqId(String.valueOf(model.getMqId()));
        expected.setMstId(String.valueOf(model.getMstId()));
        expected.setStrassenname(model.getStrassenname());
        expected.setLageMessquerschnitt(model.getBeschreibung());
        expected.setFahrtrichtung(model.getFahrtrichtung());
        expected.setAnzahlFahrspuren(model.getAnzahlFahrspuren());
        expected.setAnzahlDetektoren(model.getAnzahlDetektoren());

        final MessquerschnittDto mappingResult = mapperToTest.messquerschnittModel2Dto(model);

        Assertions.assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void messfaehigkeitModel2Dto() {
        final var model = new MessfaehigkeitModel();
        model.setGueltigAb(LocalDate.of(2024, 2, 4));
        model.setGueltigBis(LocalDate.of(2024, 2, 4));
        model.setFahrzeugklasse(Fahrzeugklasse.SUMME_KFZ);
        model.setIntervall(ZaehldatenIntervall.STUNDE_VIERTEL);

        final var expected = new MessfaehigkeitDto();
        expected.setGueltigAb(model.getGueltigAb());
        expected.setGueltigBis(model.getGueltigBis());
        expected.setFahrzeugklasse(model.getFahrzeugklasse());
        expected.setIntervall(model.getIntervall());

        final MessfaehigkeitDto mappingResult = mapperToTest.messfaehigkeitModel2Dto(model);

        Assertions.assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void featureMessquerschnittModel2Dto() {
        final var model = new MessquerschnittModel();
        model.setMqId(12345);
        model.setMstId(123);
        model.setStrassenname("setStrassenname");
        model.setBeschreibung("setBeschreibung");
        model.setFahrtrichtung("S");
        model.setAnzahlFahrspuren(1);
        model.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        model.setHersteller("setHersteller");
        model.setAnzahlDetektoren(2);

        final PointGeometryModel geometry = new PointGeometryModel();
        geometry.setType("Point");
        final double longitude = 11.689417746072813;
        final double latitude = 48.13827590664355;
        geometry.setCoordinates(List.of(BigDecimal.valueOf(longitude),
                BigDecimal.valueOf(latitude)));
        final FeatureModel<MessquerschnittModel> featureModel = new FeatureModel<>();
        featureModel.setProperties(model);
        featureModel.setGeometry(geometry);

        final var expected = new MessquerschnittDto();
        expected.setMqId(String.valueOf(model.getMqId()));
        expected.setMstId(String.valueOf(model.getMstId()));
        expected.setStrassenname(model.getStrassenname());
        expected.setLageMessquerschnitt(model.getBeschreibung());
        expected.setFahrtrichtung(model.getFahrtrichtung());
        expected.setAnzahlFahrspuren(model.getAnzahlFahrspuren());
        expected.setAnzahlDetektoren(model.getAnzahlDetektoren());
        expected.setLongitude(longitude);
        expected.setLatitude(latitude);

        final MessquerschnittDto mappingResult = mapperToTest.featureMessquerschnittModel2Dto(featureModel);

        Assertions.assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void messstelleModel2Dto() {
        final var model = new MessstelleModel();
        model.setMstId(0);
        model.setStatus(MessstelleStatus.IN_PLANUNG);
        model.setAbbaudatum(LocalDate.now());
        model.setRealisierungsdatum(LocalDate.of(2005, 11, 11));
        model.setName("setName");
        model.setStadtbezirkNummer(12);
        model.setBemerkung("setBemerkung");
        model.setDatumLetztePlausibleMessung(LocalDate.now().minusDays(1));
        model.setMessquerschnitte(new FeatureCollectionModel<>());
        model.setMessfaehigkeiten(new FeatureCollectionModel<>());

        final var expected = new MessstelleDto();
        expected.setMstId(String.valueOf(model.getMstId()));
        expected.setStatus(model.getStatus());
        expected.setRealisierungsdatum(model.getRealisierungsdatum());
        expected.setAbbaudatum(model.getAbbaudatum());
        expected.setName(model.getName());
        expected.setStadtbezirkNummer(model.getStadtbezirkNummer());
        expected.setBemerkung(model.getBemerkung());
        expected.setDatumLetztePlausibleMessung(model.getDatumLetztePlausibleMessung());

        final MessstelleDto mappingResult = mapperToTest.messstelleModel2Dto(model);

        Assertions.assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("fahrzeugKlassen", "hersteller", "detektierteVerkehrsart", "messfaehigkeiten", "messquerschnitte", "latitude", "longitude")
                .isEqualTo(expected);
    }

    @Test
    void messstelleModel2DtoAfterMapping() {
        final var model = new MessstelleModel();
        model.setMstId(0);
        model.setStatus(MessstelleStatus.IN_PLANUNG);
        model.setAbbaudatum(LocalDate.now());
        model.setRealisierungsdatum(LocalDate.of(2005, 11, 11));
        model.setName("setName");
        model.setStadtbezirkNummer(12);
        model.setBemerkung("setBemerkung");
        model.setDatumLetztePlausibleMessung(LocalDate.now().minusDays(1));

        final PointGeometryModel geometry = new PointGeometryModel();
        geometry.setType("Point");
        geometry.setCoordinates(List.of(BigDecimal.valueOf(11.689417746072813),
                BigDecimal.valueOf(48.13827590664355)));

        final FeatureCollectionModel<FeatureModel<MessquerschnittModel>> featureCollectionMessquerschnittModel = new FeatureCollectionModel<>();
        featureCollectionMessquerschnittModel.setFeatures(new ArrayList<>());
        final FeatureModel<MessquerschnittModel> messquerschnittModelFeatureModel = new FeatureModel<>();
        messquerschnittModelFeatureModel.setGeometry(geometry);
        final MessquerschnittModel messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        messquerschnittModel.setHersteller("setHersteller");
        messquerschnittModelFeatureModel.setProperties(messquerschnittModel);
        featureCollectionMessquerschnittModel.getFeatures().add(messquerschnittModelFeatureModel);
        model.setMessquerschnitte(featureCollectionMessquerschnittModel);

        final FeatureCollectionModel<FeatureModel<MessfaehigkeitModel>> featureCollectionMessfaehigkeitModel = new FeatureCollectionModel<>();
        featureCollectionMessfaehigkeitModel.setFeatures(new ArrayList<>());

        final FeatureModel<MessfaehigkeitModel> messfaehigkeitModelFeatureModel = new FeatureModel<>();
        final MessfaehigkeitModel messfaehigkeitModel = new MessfaehigkeitModel();
        messfaehigkeitModel.setFahrzeugklasse(Fahrzeugklasse.ZWEI_PLUS_EINS);
        messfaehigkeitModel.setGueltigAb(LocalDate.of(2000, 1, 1));
        messfaehigkeitModel.setGueltigBis(LocalDate.of(2015, 1, 1));
        messfaehigkeitModelFeatureModel.setProperties(messfaehigkeitModel);

        final FeatureModel<MessfaehigkeitModel> messfaehigkeitModelFeatureModel1 = new FeatureModel<>();
        final MessfaehigkeitModel messfaehigkeitModel1 = new MessfaehigkeitModel();
        messfaehigkeitModel1.setFahrzeugklasse(Fahrzeugklasse.ACHT_PLUS_EINS);
        messfaehigkeitModel1.setGueltigAb(LocalDate.of(2020, 1, 1));
        messfaehigkeitModel1.setGueltigBis(LocalDate.MAX);
        messfaehigkeitModelFeatureModel1.setProperties(messfaehigkeitModel1);

        featureCollectionMessfaehigkeitModel.getFeatures().add(messfaehigkeitModelFeatureModel1);
        featureCollectionMessfaehigkeitModel.getFeatures().add(messfaehigkeitModelFeatureModel1);
        model.setMessfaehigkeiten(featureCollectionMessfaehigkeitModel);

        final var expected = new MessstelleDto();
        expected.setMstId(String.valueOf(model.getMstId()));
        expected.setStatus(model.getStatus());
        expected.setRealisierungsdatum(model.getRealisierungsdatum());
        expected.setAbbaudatum(model.getAbbaudatum());
        expected.setName(model.getName());
        expected.setStadtbezirkNummer(model.getStadtbezirkNummer());
        expected.setBemerkung(model.getBemerkung());
        expected.setHersteller(messquerschnittModel.getHersteller());
        expected.setDetektierteVerkehrsart(messquerschnittModel.getDetektierteVerkehrsart());
        expected.setFahrzeugklasse(messfaehigkeitModel1.getFahrzeugklasse());
        expected.setDatumLetztePlausibleMessung(model.getDatumLetztePlausibleMessung());

        final MessstelleDto mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);

        Assertions.assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("fahrzeugKlassen", "detektierteVerkehrsarten", "messfaehigkeiten", "messquerschnitte", "longitude", "latitude")
                .isEqualTo(expected);
    }

    @Test
    void featureMessstelleModel2Dto() {
        final var model = new MessstelleModel();
        model.setMstId(0);
        model.setStatus(MessstelleStatus.IN_PLANUNG);
        model.setAbbaudatum(LocalDate.now());
        model.setRealisierungsdatum(LocalDate.of(2005, 11, 11));
        model.setName("setName");
        model.setStadtbezirkNummer(12);
        model.setBemerkung("setBemerkung");
        model.setDatumLetztePlausibleMessung(LocalDate.now().minusDays(1));

        final FeatureModel<MessstelleModel> featureModel = new FeatureModel<>();
        featureModel.setProperties(model);
        final PointGeometryModel geometry = new PointGeometryModel();
        geometry.setType("Point");
        final double longitude = 11.689417746072813;
        final double latitude = 48.13827590664355;
        geometry.setCoordinates(List.of(BigDecimal.valueOf(longitude),
                BigDecimal.valueOf(latitude)));
        featureModel.setGeometry(geometry);

        final FeatureCollectionModel<FeatureModel<MessquerschnittModel>> featureCollectionMessquerschnittModel = new FeatureCollectionModel<>();
        featureCollectionMessquerschnittModel.setFeatures(new ArrayList<>());
        final FeatureModel<MessquerschnittModel> messquerschnittModelFeatureModel = new FeatureModel<>();
        messquerschnittModelFeatureModel.setGeometry(geometry);
        final MessquerschnittModel messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        messquerschnittModel.setHersteller("setHersteller");
        messquerschnittModelFeatureModel.setProperties(messquerschnittModel);
        featureCollectionMessquerschnittModel.getFeatures().add(messquerschnittModelFeatureModel);
        model.setMessquerschnitte(featureCollectionMessquerschnittModel);

        final FeatureCollectionModel<FeatureModel<MessfaehigkeitModel>> featureCollectionMessfaehigkeitModel = new FeatureCollectionModel<>();
        featureCollectionMessfaehigkeitModel.setFeatures(new ArrayList<>());

        final FeatureModel<MessfaehigkeitModel> messfaehigkeitModelFeatureModel = new FeatureModel<>();
        final MessfaehigkeitModel messfaehigkeitModel = new MessfaehigkeitModel();
        messfaehigkeitModel.setFahrzeugklasse(Fahrzeugklasse.ZWEI_PLUS_EINS);
        messfaehigkeitModel.setGueltigAb(LocalDate.of(2000, 1, 1));
        messfaehigkeitModel.setGueltigBis(LocalDate.of(2015, 1, 1));
        messfaehigkeitModelFeatureModel.setProperties(messfaehigkeitModel);

        final FeatureModel<MessfaehigkeitModel> messfaehigkeitModelFeatureModel1 = new FeatureModel<>();
        final MessfaehigkeitModel messfaehigkeitModel1 = new MessfaehigkeitModel();
        messfaehigkeitModel1.setFahrzeugklasse(Fahrzeugklasse.ACHT_PLUS_EINS);
        messfaehigkeitModel1.setGueltigAb(LocalDate.of(2020, 1, 1));
        messfaehigkeitModel1.setGueltigBis(LocalDate.MAX);
        messfaehigkeitModelFeatureModel1.setProperties(messfaehigkeitModel1);

        featureCollectionMessfaehigkeitModel.getFeatures().add(messfaehigkeitModelFeatureModel1);
        featureCollectionMessfaehigkeitModel.getFeatures().add(messfaehigkeitModelFeatureModel1);
        model.setMessfaehigkeiten(featureCollectionMessfaehigkeitModel);

        final var expected = new MessstelleDto();
        expected.setMstId(String.valueOf(model.getMstId()));
        expected.setStatus(model.getStatus());
        expected.setRealisierungsdatum(model.getRealisierungsdatum());
        expected.setAbbaudatum(model.getAbbaudatum());
        expected.setName(model.getName());
        expected.setStadtbezirkNummer(model.getStadtbezirkNummer());
        expected.setBemerkung(model.getBemerkung());
        expected.setHersteller(messquerschnittModel.getHersteller());
        expected.setDetektierteVerkehrsart(messquerschnittModel.getDetektierteVerkehrsart());
        expected.setFahrzeugklasse(messfaehigkeitModel1.getFahrzeugklasse());
        expected.setDatumLetztePlausibleMessung(model.getDatumLetztePlausibleMessung());
        expected.setLongitude(longitude);
        expected.setLatitude(latitude);

        final MessstelleDto mappingResult = mapperToTest.featureMessstelleModel2Dto(featureModel);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);

        Assertions.assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("messfaehigkeiten", "messquerschnitte")
                .isEqualTo(expected);
    }

    @Test
    void messstelleModel2DtoAfterMappingDetektierteVerkehrsarten() {
        final var model = new MessstelleModel();

        final PointGeometryModel geometry = new PointGeometryModel();
        geometry.setType("Point");
        geometry.setCoordinates(List.of(BigDecimal.valueOf(11.689417746072813),
                BigDecimal.valueOf(48.13827590664355)));

        final FeatureCollectionModel<FeatureModel<MessquerschnittModel>> featureCollectionMessquerschnittModel = new FeatureCollectionModel<>();
        featureCollectionMessquerschnittModel.setFeatures(new ArrayList<>());
        final FeatureModel<MessquerschnittModel> messquerschnittModelFeatureModel = new FeatureModel<>();
        messquerschnittModelFeatureModel.setGeometry(geometry);
        final MessquerschnittModel messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        messquerschnittModelFeatureModel.setProperties(messquerschnittModel);
        featureCollectionMessquerschnittModel.getFeatures().add(messquerschnittModelFeatureModel);
        model.setMessquerschnitte(featureCollectionMessquerschnittModel);

        MessstelleDto mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(messquerschnittModel.getDetektierteVerkehrsart());

        messquerschnittModel.setDetektierteVerkehrsart(Verkehrsart.RAD);
        mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(messquerschnittModel.getDetektierteVerkehrsart());

        messquerschnittModel.setDetektierteVerkehrsart(Verkehrsart.UNBEKANNT);
        mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(messquerschnittModel.getDetektierteVerkehrsart());

        messquerschnittModel.setDetektierteVerkehrsart(null);
        mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(Verkehrsart.UNBEKANNT);

        final FeatureModel<MessquerschnittModel> messquerschnittModelFeatureModel2 = new FeatureModel<>();
        messquerschnittModelFeatureModel2.setGeometry(geometry);
        final MessquerschnittModel messquerschnittModel2 = new MessquerschnittModel();
        messquerschnittModel2.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        messquerschnittModelFeatureModel2.setProperties(messquerschnittModel2);
        featureCollectionMessquerschnittModel.getFeatures().add(messquerschnittModelFeatureModel2);
        messquerschnittModel.setDetektierteVerkehrsart(null);
        mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(Verkehrsart.KFZ);

        messquerschnittModel.setDetektierteVerkehrsart(null);
        messquerschnittModel2.setDetektierteVerkehrsart(null);
        mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(Verkehrsart.UNBEKANNT);

        messquerschnittModel.setDetektierteVerkehrsart(Verkehrsart.KFZ);
        messquerschnittModel2.setDetektierteVerkehrsart(Verkehrsart.RAD);
        mappingResult = mapperToTest.messstelleModel2Dto(model);
        mapperToTest.messstelleModel2DtoAfterMapping(mappingResult, model);
        Assertions.assertThat(mappingResult.getDetektierteVerkehrsart()).isEqualTo(Verkehrsart.UNBEKANNT);
    }

}
