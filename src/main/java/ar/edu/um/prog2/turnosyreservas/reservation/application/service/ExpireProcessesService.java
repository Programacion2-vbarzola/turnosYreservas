package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ExpireProcessesService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpireProcessesService.class);

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;

    ExpireProcessesService(ReservationProcessRepositoryPort reservationProcessRepositoryPort) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expireStaleProcesses() {
        List<ProcessStatus> nonTerminalStatuses = List.of(
            ProcessStatus.HELD,
            ProcessStatus.WAITING_FOR_PHONE,
            ProcessStatus.PHONE_REQUESTED
        );
        Instant now = Instant.now();

        var expired = reservationProcessRepositoryPort.findByStatusInAndExpiresAtBefore(nonTerminalStatuses, now);

        if (expired.isEmpty()) {
            return;
        }

        LOG.info("Expiring {} stale reservation processes", expired.size());

        expired.forEach(process -> {
            process.setStatus(ProcessStatus.EXPIRED);
            process.setUpdatedAt(now);
            reservationProcessRepositoryPort.save(process);
            LOG.debug("Expired process id={} externalProcessId={}", process.getId(), process.getExternalProcessId());
        });
    }
}
