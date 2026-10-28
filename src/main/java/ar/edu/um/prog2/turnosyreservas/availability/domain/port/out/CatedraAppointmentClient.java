package ar.edu.um.prog2.turnosyreservas.availability.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.availability.domain.model.Occupancy;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CatedraAppointmentClient {
    List<Occupancy> getOccupancies(Long professionalId, LocalDate from, LocalDate to);
    record HoldResponse(String externalHoldId, String externalProcessId, Instant expiresAt) {}
    HoldResponse createHold(Long professionalId, LocalDate date, LocalTime startTime, String externalPatientId);
}
