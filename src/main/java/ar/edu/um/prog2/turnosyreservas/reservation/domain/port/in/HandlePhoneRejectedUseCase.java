package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

import java.time.Instant;

public interface HandlePhoneRejectedUseCase {
    void handleRejected(String externalProcessId, Instant newExpiresAt);
}
