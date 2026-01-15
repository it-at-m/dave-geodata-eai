package de.muenchen.dave.geodataeai.infrastructure.adapter;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UnixMilliTimestampToLocalDateConverterTest {

    private final UnixMilliTimestampToLocalDateConverter toTest = new UnixMilliTimestampToLocalDateConverter();

    @Test
    void convert() {
        final long epochMilliSecondsAtTime = 1405268590383L;

        final LocalDate expected = LocalDate.of(2014, 7, 13);

        Assertions.assertThat(toTest.convert(epochMilliSecondsAtTime))
                .isNotNull()
                .isEqualTo(expected);
    }

}
