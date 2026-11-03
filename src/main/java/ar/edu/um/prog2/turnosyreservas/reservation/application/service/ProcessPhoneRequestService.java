package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessExpiredException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.ProcessPhoneRequestUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
class ProcessPhoneRequestService implements ProcessPhoneRequestUseCase {

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;

    ProcessPhoneRequestService(ReservationProcessRepositoryPort reservationProcessRepositoryPort) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
    }

    @Override
    public void processPhoneRequest(String externalProcessId) {
        ReservationProcess process = reservationProcessRepositoryPort
            .findByExternalProcessId(externalProcessId)
            .orElseThrow(() -> new ProcessNotFoundException(externalProcessId));

        if (process.getExpiresAt().isBefore(Instant.now())) {
            throw new ProcessExpiredException(externalProcessId);
        }

        process.setStatus(ProcessStatus.PHONE_REQUESTED);
        process.setUpdatedAt(Instant.now());
        reservationProcessRepositoryPort.save(process);
    }
}
