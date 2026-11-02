package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmHoldRequest(@NotBlank String patientFirstName, @NotBlank String patientLastName) {}
