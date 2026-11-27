package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ReservationStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleAppointmentCancelledUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationRepositoryPort;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class HandleAppointmentCancelledService implements HandleAppointmentCancelledUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(HandleAppointmentCancelledService.class);

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final ReservationRepositoryPort reservationRepositoryPort;

    HandleAppointmentCancelledService(
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        ReservationRepositoryPort reservationRepositoryPort
    ) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    @Override
    public void handleCancelled(String externalProcessId, String reason) {
        Optional<ReservationProcess> optProcess = reservationProcessRepositoryPort.findByExternalProcessId(externalProcessId);
        if (optProcess.isEmpty()) {
            LOG.warn("AppointmentCancelled received for unknown process: {}", externalProcessId);
            return;
        }

        ReservationProcess process = optProcess.get();
        if (process.getStatus() == ProcessStatus.CANCELLED) {
            return;
        }

        Instant now = Instant.now();

        reservationRepositoryPort
            .findByProcessId(process.getId())
            .ifPresent(reservation -> {
                reservation.setStatus(ReservationStatus.CANCELLED);
                reservation.setCancelledAt(now);
                reservation.setCancellationReason(reason);
                reservationRepositoryPort.save(reservation);
            });

        process.setStatus(ProcessStatus.CANCELLED);
        process.setUpdatedAt(now);
        reservationProcessRepositoryPort.save(process);
    }
}
