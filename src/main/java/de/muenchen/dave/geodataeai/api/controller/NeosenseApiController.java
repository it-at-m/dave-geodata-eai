package de.muenchen.dave.geodataeai.api.controller;

import de.muenchen.dave.geodataeai.api.controller.test.CountingUnit;
import de.muenchen.dave.geodataeai.api.controller.test.TrafficMeasurementSite;
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

    @GetMapping("the-neosense-url-for-interval-data-from-traffic-measurement-site")
    public ResponseEntity<TrafficMeasurementSite> getIntervalDataForTrafficMeasurementSite(
            @RequestParam(value = "traffic-measurement-site-id", required = true) @NotEmpty final String trafficMeasurementSiteId,
            @RequestParam(value = "from", required = true) @NotNull final LocalDateTime from,
            @RequestParam(value = "to", required = true) @NotNull final LocalDateTime to) {
        final var measurementSite = new TrafficMeasurementSite();
        return ResponseEntity.ok(measurementSite);
    }

    @GetMapping("the-neosense-url-for-interval-data-from-counting-units")
    public ResponseEntity<List<CountingUnit>> getIntervalDataForCountingUnitIds(
            @RequestParam(value = "counting-unit-ids", required = true) @NotEmpty final List<@NotEmpty String> countingUnitIds,
            @RequestParam(value = "from", required = true) @NotNull final LocalDateTime from,
            @RequestParam(value = "to", required = true) @NotNull final LocalDateTime to) {
        final var countingUnit = new CountingUnit();
        return ResponseEntity.ok(List.of(countingUnit));
    }

}
