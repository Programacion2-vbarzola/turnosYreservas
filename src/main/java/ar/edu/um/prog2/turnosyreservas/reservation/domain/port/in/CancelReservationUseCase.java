package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;

public interface CancelReservationUseCase {
    record CancelReservationResult(Long reservationProcessId, String status, Instant cancelledAt) {}

    CancelReservationResult cancelReservation(Long reservationProcessId, String reason, User user);
}
