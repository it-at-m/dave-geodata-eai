package de.muenchen.dave.geodataeai.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.mapper.FeatureResponseDomainMapperImpl;
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
import java.util.ArrayList;
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
class MessstelleServiceTest {

    @Mock
    private ArcgisRestClient arcgisRestClient;

    private MessstelleService messstelleService;

    @BeforeEach
    public void beforeEach() {
        this.messstelleService = new MessstelleService(
                arcgisRestClient,
                new FeatureResponseDomainMapperImpl(),
                "url-messstelle",
                "url-messquerschnitt",
                "url-messfaehigkeit");
        Mockito.reset(arcgisRestClient);
    }

    @Test
    void getMessstellen() throws FeatureRequestFailedException {
        final var featuresMessstellen = new ArrayList<Feature<Messstelle>>();
        var messstelleFeature = new Feature<Messstelle>();
        var messstelle = new Messstelle();
        messstelle.setMstId(1111);
        messstelle.setStadtbezirkNummer(1111);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        messstelleFeature = new Feature<>();
        messstelle = new Messstelle();
        messstelle.setMstId(2222);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        messstelleFeature = new Feature<>();
        messstelle = new Messstelle();
        messstelle.setMstId(3333);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        messstelleFeature = new Feature<>();
        messstelle = new Messstelle();
        messstelle.setMstId(4444);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        final var messstellenFeatureCollection = new FeatureCollection<Feature<Messstelle>>();
        messstellenFeatureCollection.setFeatures(featuresMessstellen);

        Mockito.when(
                arcgisRestClient.extractFeature(
                        "url-messstelle",
                        MessstelleService.WHERE_CLAUSE_MESSSTELLEN,
                        new ParameterizedTypeReference<FeatureCollection<Feature<Messstelle>>>() {
                        }))
                .thenReturn(messstellenFeatureCollection);

        final var featuresMessquerschnitt = new ArrayList<Feature<Messquerschnitt>>();
        var messquerschnittFeature = new Feature<Messquerschnitt>();
        var messquerschnitt = new Messquerschnitt();
        messquerschnitt.setMqId(1111);
        messquerschnitt.setMstId(1111);
        messquerschnittFeature.setProperties(messquerschnitt);
        featuresMessquerschnitt.add(messquerschnittFeature);

        messquerschnittFeature = new Feature<>();
        messquerschnitt = new Messquerschnitt();
        messquerschnitt.setMqId(2222);
        messquerschnitt.setMstId(2222);
        messquerschnittFeature.setProperties(messquerschnitt);
        featuresMessquerschnitt.add(messquerschnittFeature);

        messquerschnittFeature = new Feature<>();
        messquerschnitt = new Messquerschnitt();
        messquerschnitt.setMqId(3331);
        messquerschnitt.setMstId(3333);
        messquerschnittFeature.setProperties(messquerschnitt);
        featuresMessquerschnitt.add(messquerschnittFeature);

        messquerschnittFeature = new Feature<>();
        messquerschnitt = new Messquerschnitt();
        messquerschnitt.setMqId(3333);
        messquerschnitt.setMstId(3333);
        messquerschnittFeature.setProperties(messquerschnitt);
        featuresMessquerschnitt.add(messquerschnittFeature);

        messquerschnittFeature = new Feature<>();
        messquerschnitt = new Messquerschnitt();
        messquerschnitt.setMqId(4444);
        messquerschnitt.setMstId(4444);
        messquerschnittFeature.setProperties(messquerschnitt);
        featuresMessquerschnitt.add(messquerschnittFeature);

        final var messquerschnittFeatureCollection = new FeatureCollection<Feature<Messquerschnitt>>();
        messquerschnittFeatureCollection.setFeatures(featuresMessquerschnitt);

        Mockito.when(
                arcgisRestClient.extractFeature(
                        "url-messquerschnitt",
                        "MST_ID IN (1111,2222,3333,4444)",
                        new ParameterizedTypeReference<FeatureCollection<Feature<Messquerschnitt>>>() {
                        }))
                .thenReturn(messquerschnittFeatureCollection);

        final var featuresMessfaehigkeit = new ArrayList<Feature<Messfaehigkeit>>();
        var messfaehigkeitFeature = new Feature<Messfaehigkeit>();
        var messfaehigkeit = new Messfaehigkeit();
        messfaehigkeit.setMstId(1111);
        messfaehigkeitFeature.setProperties(messfaehigkeit);
        featuresMessfaehigkeit.add(messfaehigkeitFeature);

        messfaehigkeitFeature = new Feature<>();
        messfaehigkeit = new Messfaehigkeit();
        messfaehigkeit.setMstId(2222);
        messfaehigkeitFeature.setProperties(messfaehigkeit);
        featuresMessfaehigkeit.add(messfaehigkeitFeature);

        messfaehigkeitFeature = new Feature<>();
        messfaehigkeit = new Messfaehigkeit();
        messfaehigkeit.setMstId(3333);
        messfaehigkeitFeature.setProperties(messfaehigkeit);
        featuresMessfaehigkeit.add(messfaehigkeitFeature);

        messfaehigkeitFeature = new Feature<>();
        messfaehigkeit = new Messfaehigkeit();
        messfaehigkeit.setMstId(4444);
        messfaehigkeitFeature.setProperties(messfaehigkeit);
        featuresMessfaehigkeit.add(messfaehigkeitFeature);

        final var messfaehigkeitFeatureCollection = new FeatureCollection<Feature<Messfaehigkeit>>();
        messfaehigkeitFeatureCollection.setFeatures(featuresMessfaehigkeit);

        Mockito.when(
                arcgisRestClient.extractFeature(
                        "url-messfaehigkeit",
                        "MST_ID IN (1111,2222,3333,4444)",
                        new ParameterizedTypeReference<FeatureCollection<Feature<Messfaehigkeit>>>() {
                        }))
                .thenReturn(messfaehigkeitFeatureCollection);

        final var result = messstelleService.getMessstellen();

        final var expected = new FeatureCollectionModel<FeatureModel<MessstelleModel>>();

        final var featuresMessstelleModels = new ArrayList<FeatureModel<MessstelleModel>>();
        var messstelleFeatureModel = new FeatureModel<MessstelleModel>();
        var messstelleModel = new MessstelleModel();

        var featuresMessfaehigkeitModels = new ArrayList<FeatureModel<MessfaehigkeitModel>>();
        var messfaehigkeitFeatureModel = new FeatureModel<MessfaehigkeitModel>();
        var messfaehigkeitModel = new MessfaehigkeitModel();
        messfaehigkeitModel.setMstId(1111);
        messfaehigkeitFeatureModel.setProperties(messfaehigkeitModel);
        featuresMessfaehigkeitModels.add(messfaehigkeitFeatureModel);
        var messfaehigkeitFeatureCollectionModel = new FeatureCollectionModel<FeatureModel<MessfaehigkeitModel>>();
        messfaehigkeitFeatureCollectionModel.setFeatures(featuresMessfaehigkeitModels);
        messstelleModel.setMessfaehigkeiten(messfaehigkeitFeatureCollectionModel);

        var featuresMessquerschnittModels = new ArrayList<FeatureModel<MessquerschnittModel>>();
        var messquerschnittFeatureModel = new FeatureModel<MessquerschnittModel>();
        var messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setMqId(1111);
        messquerschnittModel.setMstId(1111);
        messquerschnittFeatureModel.setProperties(messquerschnittModel);
        featuresMessquerschnittModels.add(messquerschnittFeatureModel);
        var messquerschnittFeatureCollectionModel = new FeatureCollectionModel<FeatureModel<MessquerschnittModel>>();
        messquerschnittFeatureCollectionModel.setFeatures(featuresMessquerschnittModels);
        messstelleModel.setMessquerschnitte(messquerschnittFeatureCollectionModel);

        messstelleModel.setMstId(1111);
        messstelleModel.setStadtbezirkNummer(1111);
        messstelleFeatureModel.setProperties(messstelleModel);
        featuresMessstelleModels.add(messstelleFeatureModel);

        messstelleFeatureModel = new FeatureModel<>();
        messstelleModel = new MessstelleModel();

        featuresMessfaehigkeitModels = new ArrayList<>();
        messfaehigkeitFeatureModel = new FeatureModel<>();
        messfaehigkeitModel = new MessfaehigkeitModel();
        messfaehigkeitModel.setMstId(2222);
        messfaehigkeitFeatureModel.setProperties(messfaehigkeitModel);
        featuresMessfaehigkeitModels.add(messfaehigkeitFeatureModel);
        messfaehigkeitFeatureCollectionModel = new FeatureCollectionModel<>();
        messfaehigkeitFeatureCollectionModel.setFeatures(featuresMessfaehigkeitModels);
        messstelleModel.setMessfaehigkeiten(messfaehigkeitFeatureCollectionModel);

        featuresMessquerschnittModels = new ArrayList<>();
        messquerschnittFeatureModel = new FeatureModel<>();
        messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setMqId(2222);
        messquerschnittModel.setMstId(2222);
        messquerschnittFeatureModel.setProperties(messquerschnittModel);
        featuresMessquerschnittModels.add(messquerschnittFeatureModel);
        messquerschnittFeatureCollectionModel = new FeatureCollectionModel<>();
        messquerschnittFeatureCollectionModel.setFeatures(featuresMessquerschnittModels);
        messstelleModel.setMessquerschnitte(messquerschnittFeatureCollectionModel);

        messstelleModel.setMstId(2222);
        messstelleModel.setStadtbezirkNummer(999);
        messstelleFeatureModel.setProperties(messstelleModel);
        featuresMessstelleModels.add(messstelleFeatureModel);

        messstelleFeatureModel = new FeatureModel<>();
        messstelleModel = new MessstelleModel();

        featuresMessfaehigkeitModels = new ArrayList<>();
        messfaehigkeitFeatureModel = new FeatureModel<>();
        messfaehigkeitModel = new MessfaehigkeitModel();
        messfaehigkeitModel.setMstId(3333);
        messfaehigkeitFeatureModel.setProperties(messfaehigkeitModel);
        featuresMessfaehigkeitModels.add(messfaehigkeitFeatureModel);
        messfaehigkeitFeatureCollectionModel = new FeatureCollectionModel<>();
        messfaehigkeitFeatureCollectionModel.setFeatures(featuresMessfaehigkeitModels);
        messstelleModel.setMessfaehigkeiten(messfaehigkeitFeatureCollectionModel);

        featuresMessquerschnittModels = new ArrayList<>();
        messquerschnittFeatureModel = new FeatureModel<>();
        messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setMqId(3331);
        messquerschnittModel.setMstId(3333);
        messquerschnittFeatureModel.setProperties(messquerschnittModel);
        featuresMessquerschnittModels.add(messquerschnittFeatureModel);
        messquerschnittFeatureModel = new FeatureModel<>();
        messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setMqId(3333);
        messquerschnittModel.setMstId(3333);
        messquerschnittFeatureModel.setProperties(messquerschnittModel);
        featuresMessquerschnittModels.add(messquerschnittFeatureModel);
        messquerschnittFeatureCollectionModel = new FeatureCollectionModel<>();
        messquerschnittFeatureCollectionModel.setFeatures(featuresMessquerschnittModels);
        messstelleModel.setMessquerschnitte(messquerschnittFeatureCollectionModel);

        messstelleModel.setMstId(3333);
        messstelleModel.setStadtbezirkNummer(999);
        messstelleFeatureModel.setProperties(messstelleModel);
        featuresMessstelleModels.add(messstelleFeatureModel);

        messstelleFeatureModel = new FeatureModel<>();
        messstelleModel = new MessstelleModel();

        featuresMessfaehigkeitModels = new ArrayList<>();
        messfaehigkeitFeatureModel = new FeatureModel<>();
        messfaehigkeitModel = new MessfaehigkeitModel();
        messfaehigkeitModel.setMstId(4444);
        messfaehigkeitFeatureModel.setProperties(messfaehigkeitModel);
        featuresMessfaehigkeitModels.add(messfaehigkeitFeatureModel);
        messfaehigkeitFeatureCollectionModel = new FeatureCollectionModel<>();
        messfaehigkeitFeatureCollectionModel.setFeatures(featuresMessfaehigkeitModels);
        messstelleModel.setMessfaehigkeiten(messfaehigkeitFeatureCollectionModel);

        featuresMessquerschnittModels = new ArrayList<>();
        messquerschnittFeatureModel = new FeatureModel<>();
        messquerschnittModel = new MessquerschnittModel();
        messquerschnittModel.setMqId(4444);
        messquerschnittModel.setMstId(4444);
        messquerschnittFeatureModel.setProperties(messquerschnittModel);
        featuresMessquerschnittModels.add(messquerschnittFeatureModel);
        messquerschnittFeatureCollectionModel = new FeatureCollectionModel<>();
        messquerschnittFeatureCollectionModel.setFeatures(featuresMessquerschnittModels);
        messstelleModel.setMessquerschnitte(messquerschnittFeatureCollectionModel);

        messstelleModel.setMstId(4444);
        messstelleModel.setStadtbezirkNummer(999);
        messstelleFeatureModel.setProperties(messstelleModel);
        featuresMessstelleModels.add(messstelleFeatureModel);

        expected.setFeatures(featuresMessstelleModels);

        assertThat(result, is(expected));

        Mockito
                .verify(arcgisRestClient, Mockito.times(1))
                .extractFeature(
                        "url-messstelle",
                        MessstelleService.WHERE_CLAUSE_MESSSTELLEN,
                        new ParameterizedTypeReference<FeatureCollection<Feature<Messstelle>>>() {
                        });
        Mockito
                .verify(arcgisRestClient, Mockito.times(1))
                .extractFeature(
                        "url-messquerschnitt",
                        "MST_ID IN (1111,2222,3333,4444)",
                        new ParameterizedTypeReference<FeatureCollection<Feature<Messquerschnitt>>>() {
                        });
        Mockito
                .verify(arcgisRestClient, Mockito.times(1))
                .extractFeature(
                        "url-messfaehigkeit",
                        "MST_ID IN (1111,2222,3333,4444)",
                        new ParameterizedTypeReference<FeatureCollection<Feature<Messfaehigkeit>>>() {
                        });

    }

    @Test
    void getCommaSeperatedMessstellenIds() {
        final var features = new ArrayList<Feature<Messstelle>>();
        var messstelleFeature = new Feature<Messstelle>();
        var messstelle = new Messstelle();
        messstelle.setMstId(1111);
        messstelleFeature.setProperties(messstelle);
        features.add(messstelleFeature);

        messstelleFeature = new Feature<>();
        messstelle = new Messstelle();
        messstelle.setMstId(2222);
        messstelleFeature.setProperties(messstelle);
        features.add(messstelleFeature);

        messstelleFeature = new Feature<>();
        messstelle = new Messstelle();
        messstelle.setMstId(3333);
        messstelleFeature.setProperties(messstelle);
        features.add(messstelleFeature);

        messstelleFeature = new Feature<>();
        messstelle = new Messstelle();
        messstelle.setMstId(4444);
        messstelleFeature.setProperties(messstelle);
        features.add(messstelleFeature);

        final var messstellenFeatureCollection = new FeatureCollection<Feature<Messstelle>>();
        messstellenFeatureCollection.setFeatures(features);

        final var result = messstelleService.getCommaSeperatedMessstellenIds(messstellenFeatureCollection);

        final var expected = "1111,2222,3333,4444";

        assertThat(result, is(expected));
    }

    @Test
    void getWhereClauseMessquerschnitt() {
        final var result = messstelleService.getWhereClauseMessquerschnitt("1111,2222,3333,4444");

        final var expected = "MST_ID IN (1111,2222,3333,4444)";

        assertThat(result, is(expected));
    }

    @Test
    void getWhereClauseMessfaehigkeit() {
        final var result = messstelleService.getWhereClauseMessfaehigkeit("1111,2222,3333,4444");

        final var expected = "MST_ID IN (1111,2222,3333,4444)";

        assertThat(result, is(expected));
    }

}
