package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessOwnerMismatchException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.GetProcessStatusUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
class GetProcessStatusService implements GetProcessStatusUseCase {

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;

    GetProcessStatusService(ReservationProcessRepositoryPort reservationProcessRepositoryPort) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
    }

    @Override
    public ProcessStatusResult getProcessStatus(Long processId, User user) {
        ReservationProcess process = reservationProcessRepositoryPort
            .findById(processId)
            .orElseThrow(() -> new ProcessNotFoundException(processId));

        if (!process.getUserId().equals(user.getId())) {
            throw new ProcessOwnerMismatchException(processId);
        }

        return new ProcessStatusResult(process.getId(), process.getStatus().name(), process.getExpiresAt(), null);
    }
}
