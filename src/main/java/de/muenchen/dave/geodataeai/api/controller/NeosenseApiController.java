package de.muenchen.dave.geodataeai.api.controller;

import de.muenchen.dave.geodataeai.api.controller.test.CountingUnitDto;
import de.muenchen.dave.geodataeai.api.controller.test.TrafficMeasurementPointDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class NeosenseApiController {

    @GetMapping("the-neosense-url-for-interval-data-from-traffic-meassurement-point")
    public ResponseEntity<TrafficMeasurementPointDto> getIntervalDataForTrafficMeasurementPoint(
            @RequestParam(value = "traffic-meassurement-point", required = true) @NotNull final Integer trafficMeasurementPoint,
            @RequestParam(value = "from", required = true) @NotNull final LocalDateTime from,
            @RequestParam(value = "to", required = true) @NotNull final LocalDateTime to) {
        final var measurementPoint = new TrafficMeasurementPointDto();
        return ResponseEntity.ok(measurementPoint);
    }

    @GetMapping("the-neosense-url-for-interval-data-from-counting-unit-ids")
    public ResponseEntity<CountingUnitDto> getIntervalDataForCountingUnitIds(
            @RequestParam(value = "counting-unit-ids", required = true) @NotEmpty final List<@NotNull Integer> countingUnitIds,
            @RequestParam(value = "from", required = true) @NotNull final LocalDateTime from,
            @RequestParam(value = "to", required = true) @NotNull final LocalDateTime to) {
        final var countingUnit = new CountingUnitDto();
        return ResponseEntity.ok(countingUnit);
    }

}
