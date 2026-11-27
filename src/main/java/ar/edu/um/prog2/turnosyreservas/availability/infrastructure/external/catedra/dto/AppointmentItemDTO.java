package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AppointmentItemDTO(
    String reservationProcessId,
    Long reservationId,
    String status,
    String patientFirstName,
    String patientLastName,
    String patientPhone,
    Instant confirmedAt,
    Instant cancelledAt,
    String cancellationReason
) {}
