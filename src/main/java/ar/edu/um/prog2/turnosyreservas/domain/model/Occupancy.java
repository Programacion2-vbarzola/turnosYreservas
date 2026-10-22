package ar.edu.um.prog2.turnosyreservas.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record Occupancy(LocalDate date, LocalTime startTime, String status) {
    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }

    public boolean isHeld() {
        return "HELD".equals(status);
    }
}
