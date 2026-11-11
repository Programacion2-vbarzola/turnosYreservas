package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleFinalStateUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
class HandleFinalStateService implements HandleFinalStateUseCase {

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;

    HandleFinalStateService(ReservationProcessRepositoryPort reservationProcessRepositoryPort) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
    }

    @Override
    public void handleFinalState(String externalProcessId, String eventType) {
        ReservationProcess process = reservationProcessRepositoryPort
            .findByExternalProcessId(externalProcessId)
            .orElseThrow(() -> new ProcessNotFoundException(externalProcessId));

        ProcessStatus newStatus =
            switch (eventType) {
                case "AdditionalInformationRejected" -> ProcessStatus.REJECTED;
                case "AppointmentProcessExpired" -> ProcessStatus.EXPIRED;
                case "AppointmentProcessInvalid" -> ProcessStatus.INVALID;
                default -> ProcessStatus.FAILED;
            };

        process.setStatus(newStatus);
        process.setUpdatedAt(Instant.now());
        reservationProcessRepositoryPort.save(process);
    }
}
