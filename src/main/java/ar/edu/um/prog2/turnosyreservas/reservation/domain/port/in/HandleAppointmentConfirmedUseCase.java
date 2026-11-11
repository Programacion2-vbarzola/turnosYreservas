package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

public interface HandleAppointmentConfirmedUseCase {
    void handleConfirmed(String externalProcessId, Long externalReservationId);
}
