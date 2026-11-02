package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record HoldRequest(@NotNull Long professionalId, @NotNull LocalDate date, @NotNull LocalTime startTime) {}
