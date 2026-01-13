package de.muenchen.dave.geodataeai.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.dave.geodataeai.domain.mapper.FeatureResponseDomainMapperImpl;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureCollectionModel;
import de.muenchen.dave.geodataeai.domain.model.feature.FeatureModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessquerschnittModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.MessstelleModel;
import de.muenchen.dave.geodataeai.domain.model.messstelle.UnauffaelligerTagModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.ZeitraumModel;
import de.muenchen.dave.geodataeai.domain.service.tagesaggregat.TagesaggregatExtractionService;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UnauffaelligeTageServiceTest {

    private UnauffaelligeTageService unauffaelligeTageService;

    @Mock
    private MessstelleService messstelleService;

    @Mock
    private TagesaggregatExtractionService tagesaggregatExtractionService;

    @BeforeEach
    public void beforeEach() {
        this.unauffaelligeTageService = new UnauffaelligeTageService(
                messstelleService,
                tagesaggregatExtractionService,
                new FeatureResponseDomainMapperImpl());
        Mockito.reset(messstelleService, tagesaggregatExtractionService);
    }

    @Test
    void getUnauffaelligeTageForEachMessstelle() throws FeatureRequestFailedException {

        final var featuresMessstellen = new ArrayList<FeatureModel<MessstelleModel>>();
        var messstelleFeature = new FeatureModel<MessstelleModel>();
        var messstelle = new MessstelleModel();
        messstelle.setMstId(4000);
        messstelle.setStadtbezirkNummer(1111);
        var messquerschnitt1 = new MessquerschnittModel();
        messquerschnitt1.setMqId(400001);
        var messquerschnittFeature1 = new FeatureModel<MessquerschnittModel>();
        messquerschnittFeature1.setProperties(messquerschnitt1);
        var featuresMessquerschnitte = new ArrayList<FeatureModel<MessquerschnittModel>>();
        featuresMessquerschnitte.add(messquerschnittFeature1);
        var messquerschnitt2 = new MessquerschnittModel();
        messquerschnitt2.setMqId(400002);
        var messquerschnittFeature2 = new FeatureModel<MessquerschnittModel>();
        messquerschnittFeature2.setProperties(messquerschnitt2);
        featuresMessquerschnitte.add(messquerschnittFeature2);
        var messquerschnittFeatureCollection = new FeatureCollectionModel<FeatureModel<MessquerschnittModel>>();
        messquerschnittFeatureCollection.setFeatures(featuresMessquerschnitte);
        messstelle.setMessquerschnitte(messquerschnittFeatureCollection);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        messstelleFeature = new FeatureModel<>();
        messstelle = new MessstelleModel();
        messstelle.setMstId(5059);
        messquerschnitt1 = new MessquerschnittModel();
        messquerschnitt1.setMqId(505999);
        messquerschnittFeature1 = new FeatureModel<>();
        messquerschnittFeature1.setProperties(messquerschnitt1);
        featuresMessquerschnitte = new ArrayList<>();
        featuresMessquerschnitte.add(messquerschnittFeature1);
        messquerschnittFeatureCollection = new FeatureCollectionModel<>();
        messquerschnittFeatureCollection.setFeatures(featuresMessquerschnitte);
        messstelle.setMessquerschnitte(messquerschnittFeatureCollection);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        messstelleFeature = new FeatureModel<>();
        messstelle = new MessstelleModel();
        messstelle.setMstId(9999);
        messquerschnitt1 = new MessquerschnittModel();
        messquerschnitt1.setMqId(999998);
        messquerschnittFeature1 = new FeatureModel<>();
        messquerschnittFeature1.setProperties(messquerschnitt1);
        featuresMessquerschnitte = new ArrayList<>();
        featuresMessquerschnitte.add(messquerschnittFeature1);
        messquerschnitt1 = new MessquerschnittModel();
        messquerschnitt1.setMqId(999999);
        messquerschnittFeature1 = new FeatureModel<>();
        messquerschnittFeature1.setProperties(messquerschnitt1);
        featuresMessquerschnitte.add(messquerschnittFeature1);
        messquerschnittFeatureCollection = new FeatureCollectionModel<>();
        messquerschnittFeatureCollection.setFeatures(featuresMessquerschnitte);
        messstelle.setMessquerschnitte(messquerschnittFeatureCollection);
        messstelleFeature.setProperties(messstelle);
        featuresMessstellen.add(messstelleFeature);

        final var messstellenFeatureCollection = new FeatureCollectionModel<FeatureModel<MessstelleModel>>();
        messstellenFeatureCollection.setFeatures(featuresMessstellen);

        Mockito.when(messstelleService.getMessstellen()).thenReturn(messstellenFeatureCollection);

        final var startDate = LocalDate.of(2020, 1, 1);
        final var endDate = LocalDate.of(2020, 12, 31);

        final var tagesaggregat1 = new TagesaggregatModel();
        tagesaggregat1.setMqId(400001);
        tagesaggregat1.setDatum(LocalDateTime.of(2020, 5, 5, 0, 0, 0));
        final var tagesaggregat2 = new TagesaggregatModel();
        tagesaggregat2.setMqId(505999);
        tagesaggregat2.setDatum(LocalDateTime.of(2020, 5, 6, 0, 0, 0));
        final var tagesaggregat3 = new TagesaggregatModel();
        tagesaggregat3.setMqId(999999);
        tagesaggregat3.setDatum(LocalDateTime.of(2020, 5, 7, 0, 0, 0));
        final var tagesaggregat4 = new TagesaggregatModel();
        tagesaggregat4.setMqId(999998);
        tagesaggregat4.setDatum(LocalDateTime.of(2020, 5, 7, 0, 0, 0));
        final var tagesaggregat5 = new TagesaggregatModel();
        tagesaggregat5.setMqId(400002);
        tagesaggregat5.setDatum(LocalDateTime.of(2020, 5, 5, 0, 0, 0));

        //        var tagesaggregate = Stream.of(tagesaggregat1, tagesaggregat2, tagesaggregat3, tagesaggregat4, tagesaggregat5);
        var tagesaggregate = new ArrayList<TagesaggregatModel>();

        //        var requestedMqIds = List.of(400001, 505999, 999999, 999998);
        var requestedMqIds = new ArrayList<Integer>();

        Mockito.when(tagesaggregatExtractionService.getTagesaggregate(Mockito.argThat(argument -> {
            tagesaggregate.add(tagesaggregat2);
            requestedMqIds.add(tagesaggregat2.getMqId());
            if (argument.contains(400001) && !argument.contains(400002)) {
                tagesaggregate.add(tagesaggregat1);
                requestedMqIds.add(tagesaggregat1.getMqId());
            } else if (!argument.contains(400001) && argument.contains(400002)) {
                tagesaggregate.add(tagesaggregat5);
                requestedMqIds.add(tagesaggregat5.getMqId());
            }
            if (argument.contains(999999) && !argument.contains(999998)) {
                tagesaggregate.add(tagesaggregat3);
                requestedMqIds.add(tagesaggregat3.getMqId());
            } else if (!argument.contains(999999) && argument.contains(999998)) {
                tagesaggregate.add(tagesaggregat4);
                requestedMqIds.add(tagesaggregat4.getMqId());
            }
            return CollectionUtils.isNotEmpty(argument) &&
                    argument.size() == 3 &&
                    argument.contains(505999) &&
                    ((argument.contains(400001) && !argument.contains(400002)) ||
                            (!argument.contains(400001) && argument.contains(400002)))
                    &&
                    ((argument.contains(999999) && !argument.contains(999998)) ||
                            (!argument.contains(999999) && argument.contains(999998)));

        }),
                Mockito.eq(List.of(new ZeitraumModel(startDate, endDate))))).thenReturn(tagesaggregate.stream());

        final var result = unauffaelligeTageService.getUnauffaelligeTageForEachMessstelle(startDate, endDate);

        final var unauffaelligerTag1 = new UnauffaelligerTagModel();
        unauffaelligerTag1.setMstId("4000");
        unauffaelligerTag1.setDatum(LocalDate.of(2020, 5, 5));
        final var unauffaelligerTag2 = new UnauffaelligerTagModel();
        unauffaelligerTag2.setMstId("5059");
        unauffaelligerTag2.setDatum(LocalDate.of(2020, 5, 6));
        final var unauffaelligerTag3 = new UnauffaelligerTagModel();
        unauffaelligerTag3.setMstId("9999");
        unauffaelligerTag3.setDatum(LocalDate.of(2020, 5, 7));

        final var expected = List.of(unauffaelligerTag1, unauffaelligerTag2, unauffaelligerTag3);

        assertThat(result, containsInAnyOrder(expected.toArray()));

        Mockito.verify(tagesaggregatExtractionService, Mockito.times(1)).getTagesaggregate(
                Mockito.argThat(argument -> CollectionUtils.isEqualCollection(argument, requestedMqIds)),
                Mockito.eq(List.of(new ZeitraumModel(startDate, endDate))));
    }
}
