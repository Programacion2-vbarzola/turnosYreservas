package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

public interface HandleAppointmentCancelledUseCase {
    void handleCancelled(String externalProcessId, String reason);
}
