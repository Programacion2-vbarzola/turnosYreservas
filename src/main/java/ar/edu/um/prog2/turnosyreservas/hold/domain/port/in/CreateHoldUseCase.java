package ar.edu.um.prog2.turnosyreservas.hold.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public interface CreateHoldUseCase {
    record HoldCreatedResult(Long holdId, Long reservationProcessId, String status, Instant expiresAt) {}
    HoldCreatedResult createHold(Long professionalId, LocalDate date, LocalTime startTime, User user, String externalPatienId);
}
