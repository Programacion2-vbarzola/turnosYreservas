package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandlePhoneRejectedUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import java.time.Instant;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class HandlePhoneRejectedService implements HandlePhoneRejectedUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(HandlePhoneRejectedService.class);

    private static final Set<ProcessStatus> IRREVERSIBLE = Set.of(
        ProcessStatus.CONFIRMED,
        ProcessStatus.EXPIRED,
        ProcessStatus.INVALID,
        ProcessStatus.CANCELLED
    );

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;

    HandlePhoneRejectedService(ReservationProcessRepositoryPort reservationProcessRepositoryPort) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
    }

    @Override
    public void handleRejected(String externalProcessId, Instant newExpiresAt) {
        ReservationProcess process = reservationProcessRepositoryPort
            .findByExternalProcessId(externalProcessId)
            .orElseThrow(() -> new ProcessNotFoundException(externalProcessId));

        if (IRREVERSIBLE.contains(process.getStatus())) {
            LOG.warn(
                "AdditionalInformationRejected arrived for process {} in final state {}, ignoring",
                externalProcessId,
                process.getStatus()
            );
            return;
        }

        process.setStatus(ProcessStatus.PHONE_REQUESTED);
        if (newExpiresAt != null) {
            process.setExpiresAt(newExpiresAt);
        }
        process.setUpdatedAt(Instant.now());
        reservationProcessRepositoryPort.save(process);
    }
}
