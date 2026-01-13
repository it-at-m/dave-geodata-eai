package de.muenchen.dave.geodataeai.api.controller;

import de.muenchen.dave.geodataeai.api.dto.error.InformationResponseDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.MessstelleDto;
import de.muenchen.dave.geodataeai.api.dto.messstelle.UnauffaelligerTagDto;
import de.muenchen.dave.geodataeai.api.mapper.ResponseApiMapper;
import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.service.MessstelleService;
import de.muenchen.dave.geodataeai.domain.service.UnauffaelligeTageService;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/messstelle")
@Tag(name = "Messstelle", description = "API zum Abfragen des FeatureServers für die Messstellen.")
@Validated
public class MessstelleController {

    private final MessstelleService messstelleService;

    private final UnauffaelligeTageService unauffaelligeTageService;

    private final ResponseApiMapper responseApiMapper;

    @GetMapping
    @Operation(summary = "Holt alle relevanten Messstellen.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Messstellen erfolgreich abgefragt."),
                    @ApiResponse(
                            responseCode = "500", description = "Bei der Erstellung oder Durchführung des Requests ist ein Fehler aufgetreten.",
                            content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
                    ),
            }
    )
    @LogExecutionTime
    public ResponseEntity<List<MessstelleDto>> getMessstellen() throws FeatureRequestFailedException {
        final var model = messstelleService.getMessstellen();
        if (ObjectUtils.isNotEmpty(model) && CollectionUtils.isNotEmpty(model.getFeatures())) {
            final var dto = model.getFeatures().stream()
                    .map(responseApiMapper::featureMessstelleModel2Dto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/unauffaellige-tage-for-each-messstelle")
    @Operation(summary = "Gibt die unauffälligen Tage für jede existierende Messstelle im gegebenen Zeitraum zurück.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Unauffällige Tage erfolgreich abgefragt."),
                    @ApiResponse(
                            responseCode = "500", description = "Bei der Erstellung oder Durchführung des Requests ist ein Fehler aufgetreten.",
                            content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
                    ),
            }
    )
    @LogExecutionTime
    public ResponseEntity<List<UnauffaelligerTagDto>> getUnauffaelligeTageForEachMessstelle(
            @RequestParam(name = "start-date") @NotNull final LocalDate startDate,
            @RequestParam(name = "end-date") @NotNull final LocalDate endDate) throws FeatureRequestFailedException {
        final var models = unauffaelligeTageService.getUnauffaelligeTageForEachMessstelle(startDate, endDate);
        final var dtos = responseApiMapper.model2Dto(models);
        return ResponseEntity.ok(dtos);
    }
}
