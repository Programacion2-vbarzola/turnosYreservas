package ar.edu.um.prog2.turnosyreservas.availability.domain.model;

import java.time.LocalTime;

public record AvailabilitySlot(LocalTime startTime, LocalTime endTime, boolean available) {}
