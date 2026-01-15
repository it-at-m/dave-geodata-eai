package de.muenchen.dave.geodataeai.domain.model.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum IntervalSize {

    INTERVAL_15(15, 96),

    INTERVAL_30(30, 48),

    INTERVAL_60(60, 24);

    private static final Map<Long, IntervalSize> intervalSizeByMinutes = Stream
            .of(IntervalSize.values())
            .collect(Collectors.toMap(IntervalSize::getMinutes, Function.identity()));
    @Getter
    private final long minutes;

    @Getter
    private final int intervalsPerDay;

    public static IntervalSize getByMinutes(final long minutes) {
        return intervalSizeByMinutes.get(minutes);
    }

}
