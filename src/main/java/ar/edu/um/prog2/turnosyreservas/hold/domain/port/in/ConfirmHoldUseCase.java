package ar.edu.um.prog2.turnosyreservas.hold.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;

public interface ConfirmHoldUseCase {
    record ConfirmHoldResult(Long reservationProcessId, String status, Instant expiresAt) {}
    ConfirmHoldResult confirmHold(Long holdId, String patientFirstName, String patientLastName, User user);
}
