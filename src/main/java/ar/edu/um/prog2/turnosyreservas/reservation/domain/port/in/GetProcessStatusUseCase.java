package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;

public interface GetProcessStatusUseCase {
    record ProcessStatusResult(Long reservationProcessId, String status, Instant expiresAt, String message) {}
    ProcessStatusResult getProcessStatus(Long processId, User user);
}
