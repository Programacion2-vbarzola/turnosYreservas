package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HoldResponseDTO(String externalHoldId, String externalProcessId, Instant expiresAt) {}
