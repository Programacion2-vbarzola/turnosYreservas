package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

public interface HandleFinalStateUseCase {
    void handleFinalState(String externalProcessId, String eventType);
}
