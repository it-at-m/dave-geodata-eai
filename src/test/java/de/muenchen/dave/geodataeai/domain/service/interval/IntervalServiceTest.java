package de.muenchen.dave.geodataeai.domain.service.interval;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.domain.model.messwerte.IntervalModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    void testCountIncludedMeasuringDays() {
        final Map<Integer, List<IntervalModel>> aggregatedIntervalsByMqId = new HashMap<>();
        List<IntervalModel> listMq1 = new ArrayList<>();
        for (int i1 = 1; i1 <= 20; i1++) {
            var model = new IntervalModel();
            model.setMqId(1);
            model.setDatumUhrzeitVon(LocalDateTime.of(2026, 3, i1, 0, 0, 0));
            listMq1.add(model);
        }
        aggregatedIntervalsByMqId.put(1, listMq1);
        List<IntervalModel> listMq2 = new ArrayList<>();
        for (int i2 = 10; i2 <= 30; i2++) {
            var model = new IntervalModel();
            model.setMqId(2);
            model.setDatumUhrzeitVon(LocalDateTime.of(2026, 3, i2, 0, 0, 0));
            listMq2.add(model);
        }
        aggregatedIntervalsByMqId.put(2, listMq2);
        assertThat(intervalService.countIncludedMeasuringDays(aggregatedIntervalsByMqId), is(30));
    }
}
