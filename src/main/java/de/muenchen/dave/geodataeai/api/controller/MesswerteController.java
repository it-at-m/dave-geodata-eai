/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.dave.geodataeai.api.controller;

import de.muenchen.dave.geodataeai.api.dto.error.InformationResponseDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.IntervalResponseDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.MesswertRequestDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.TagesaggregatRequestDto;
import de.muenchen.dave.geodataeai.api.dto.messwerte.TagesaggregatResponseDto;
import de.muenchen.dave.geodataeai.api.mapper.RequestApiMapper;
import de.muenchen.dave.geodataeai.api.mapper.ResponseApiMapper;
import de.muenchen.dave.geodataeai.configuration.LogExecutionTime;
import de.muenchen.dave.geodataeai.domain.service.interval.IntervalService;
import de.muenchen.dave.geodataeai.domain.service.tagesaggregat.TagesaggregatService;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/messwerte")
@Tag(name = "Messwerte", description = "API zum Abfragen des FeatureServers für Messwerte.")
@Validated
public class MesswerteController {

    private final IntervalService intervalService;

    private final TagesaggregatService tagesaggregatService;

    private final RequestApiMapper requestApiMapper;

    private final ResponseApiMapper responseApiMapper;

    @PostMapping(value = "intervals")
    @Operation(
            summary = "Extrahiert die Intervalle und führt eine Aggregation, Summierung und Durchschnittsbildung auf die Intervalle durch. " +
                    "Zum einen wird der Durchschnitt für jeden Messtag je Messquerschnitt der Messstelle über alle Tagesintervalle gebildet." +
                    "Und zum anderen der Durchschnitt für jeden Messtag je Interval über die selektierten Messquerschnitte."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Intervalle erfolgreich abgefragt."),
                    @ApiResponse(
                            responseCode = "500", description = "Bei der Erstellung oder Durchführung des Requests ist ein Fehler aufgetreten.",
                            content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
                    ),
            }
    )
    @LogExecutionTime
    public ResponseEntity<IntervalResponseDto> getIntervalle(@RequestBody @Valid @NotNull final MesswertRequestDto request)
            throws FeatureRequestFailedException {
        final var requestModel = requestApiMapper.dto2Model(request);
        final var responseModel = intervalService.getIntervals(requestModel);
        return ResponseEntity.ok(responseApiMapper.model2Dto(responseModel));
    }

    @PostMapping(value = "daily-aggregates")
    @Operation(
            summary = "Extrahiert die Tagesaggregate und führt eine Durchschnittsbildung auf die Tagesaggregate pro Messquerschnitt durch."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Tagesaggregate erfolgreich abgefragt."),
                    @ApiResponse(
                            responseCode = "500", description = "Bei der Erstellung oder Durchführung des Requests ist ein Fehler aufgetreten.",
                            content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
                    ),
            }
    )
    @LogExecutionTime
    public ResponseEntity<TagesaggregatResponseDto> getMeanOfDailyAggregatesPerMQ(@RequestBody @Valid @NotNull final TagesaggregatRequestDto request)
            throws FeatureRequestFailedException {
        final var requestModel = requestApiMapper.dto2Model(request);
        final var responseModel = tagesaggregatService.getMeanOfTagesaggregateForAllMqIds(requestModel);
        return ResponseEntity.ok(responseApiMapper.model2Dto(responseModel));
    }

}
