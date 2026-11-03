package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SubmitPhoneRequest(
    @NotBlank @Pattern(regexp = "\\+?[0-9\\s\\-()]{7,15}", message = "Invalid phone number format") String phoneNumber
) {}
