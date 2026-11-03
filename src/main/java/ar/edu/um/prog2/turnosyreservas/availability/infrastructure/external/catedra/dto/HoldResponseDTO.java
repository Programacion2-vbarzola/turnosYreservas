package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HoldResponseDTO(
    @JsonProperty("holdId") String externalHoldId,
    @JsonProperty("reservationProcessId") String externalProcessId,
    Instant expiresAt
) {}
