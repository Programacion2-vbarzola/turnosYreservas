package ar.edu.um.prog2.turnosyreservas.domain.model;

import java.time.LocalTime;

public record ScheduleSlot(LocalTime startTime, LocalTime endTime) {}
