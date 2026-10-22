package ar.edu.um.prog2.turnosyreservas.domain.model;

import java.time.LocalTime;

public record AvailabilitySlot(LocalTime startTime, LocalTime endTime, boolean available) {}
