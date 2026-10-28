package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record HoldRequestDTO(Long professionalId, LocalDate date, LocalTime startTime, String externalPatientId) {}
