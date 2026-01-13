package de.muenchen.dave.geodataeai.domain.service.interval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.model.enums.IntervalSize;
import de.muenchen.dave.geodataeai.domain.model.messwerte.MesswertRequestModel;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntervalServiceTest {

    private IntervalService intervalService;

    @BeforeEach
    void beforeEach() {
        intervalService = new IntervalService(null, null, null);
    }

    @Test
    void getIncludedMeasuringDays() {
        final MesswertRequestModel request = new MesswertRequestModel();
        request.setIntervalInMinutes(IntervalSize.INTERVAL_15);
        request.setStartTime(LocalTime.of(0, 0, 0));
        request.setEndTime(LocalTime.of(6, 0, 0));
        int sizeOfAllAggregatesIntervals = 96;
        int sizeOfAggregatedIntervalsByMqId = 2;
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(2));

        request.setIntervalInMinutes(IntervalSize.INTERVAL_30);
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(4));

        request.setIntervalInMinutes(IntervalSize.INTERVAL_60);
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(8));

        request.setIntervalInMinutes(IntervalSize.INTERVAL_15);
        request.setStartTime(LocalTime.of(0, 0, 0));
        request.setEndTime(LocalTime.MAX);
        sizeOfAllAggregatesIntervals = 7392;
        sizeOfAggregatedIntervalsByMqId = 1;
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(77));

        request.setIntervalInMinutes(IntervalSize.INTERVAL_30);
        request.setStartTime(LocalTime.of(15, 0, 0));
        request.setEndTime(LocalTime.of(19, 0, 0));
        sizeOfAllAggregatesIntervals = 48;
        sizeOfAggregatedIntervalsByMqId = 2;
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(3));

        request.setIntervalInMinutes(IntervalSize.INTERVAL_60);
        request.setStartTime(LocalTime.of(19, 0, 0));
        request.setEndTime(LocalTime.MAX);
        sizeOfAllAggregatesIntervals = 90;
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(9));

        request.setIntervalInMinutes(IntervalSize.INTERVAL_15);
        request.setStartTime(LocalTime.of(23, 0, 0));
        request.setEndTime(LocalTime.MAX);
        sizeOfAllAggregatesIntervals = 12;
        sizeOfAggregatedIntervalsByMqId = 1;
        assertThat(intervalService.getIncludedMeasuringDays(request, sizeOfAllAggregatesIntervals, sizeOfAggregatedIntervalsByMqId), is(3));
    }
}
