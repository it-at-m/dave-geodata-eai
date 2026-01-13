package de.muenchen.dave.geodataeai.domain.service.tagesaggregat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatRequestModel;
import de.muenchen.dave.geodataeai.domain.model.messwerte.TagesaggregatResponseModel;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import org.junit.jupiter.api.Assertions;
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
class TagesaggregatServiceTest {

    private TagesaggregatService tagesaggregatService;

    @Mock
    private TagesaggregatAveragingService tagesaggregatAveragingService;

    @BeforeEach
    public void beforeEach() {
        this.tagesaggregatService = new TagesaggregatService(tagesaggregatAveragingService);
        Mockito.reset(tagesaggregatAveragingService);
    }

    @Test
    void getMeanOfTagesaggregateForAllMqIds() throws FeatureRequestFailedException {
        Mockito.when(tagesaggregatAveragingService.getMeanOfAggregatesForAllMqIds(new TagesaggregatRequestModel()))
                .thenReturn(new TagesaggregatResponseModel());

        final var result = tagesaggregatService.getMeanOfTagesaggregateForAllMqIds(new TagesaggregatRequestModel());
        final var expected = new TagesaggregatResponseModel();

        assertThat(result, is(expected));

        Mockito.verify(tagesaggregatAveragingService, Mockito.times(1)).getMeanOfAggregatesForAllMqIds(new TagesaggregatRequestModel());
    }

    @Test
    void getMeanOfTagesaggregateForAllMqIdsRuntimeException() throws FeatureRequestFailedException {
        Mockito.when(tagesaggregatAveragingService.getMeanOfAggregatesForAllMqIds(new TagesaggregatRequestModel()))
                .thenThrow(new RuntimeException("the exception"));

        Assertions.assertThrows(
                FeatureRequestFailedException.class,
                () -> this.tagesaggregatService.getMeanOfTagesaggregateForAllMqIds(new TagesaggregatRequestModel()));

        Mockito.verify(tagesaggregatAveragingService, Mockito.times(1))
                .getMeanOfAggregatesForAllMqIds(new TagesaggregatRequestModel());
    }

}
