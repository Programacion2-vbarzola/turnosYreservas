package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.availability.domain.port.out.CatedraAppointmentClient;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleAppointmentConfirmedUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserProfileRepositoryPort;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ReconcilePhoneSubmittedService {

    private static final Logger LOG = LoggerFactory.getLogger(ReconcilePhoneSubmittedService.class);

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final CatedraAppointmentClient catedraAppointmentClient;
    private final HandleAppointmentConfirmedUseCase handleAppointmentConfirmedUseCase;

    ReconcilePhoneSubmittedService(
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        UserProfileRepositoryPort userProfileRepositoryPort,
        CatedraAppointmentClient catedraAppointmentClient,
        HandleAppointmentConfirmedUseCase handleAppointmentConfirmedUseCase
    ) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
        this.userProfileRepositoryPort = userProfileRepositoryPort;
        this.catedraAppointmentClient = catedraAppointmentClient;
        this.handleAppointmentConfirmedUseCase = handleAppointmentConfirmedUseCase;
    }

    @Scheduled(fixedDelay = 120_000)
    @Transactional
    public void reconcileStuckProcesses() {
        Instant now = Instant.now();
        List<ReservationProcess> stuck = reservationProcessRepositoryPort.findByStatusInAndExpiresAtBefore(
            List.of(ProcessStatus.PHONE_SUBMITTED),
            now
        );

        if (stuck.isEmpty()) return;

        LOG.info("Reconciling {} stuck PHONE_SUBMITTED processes", stuck.size());

        for (ReservationProcess process : stuck) {
            reconcile(process, now);
        }
    }

    private void reconcile(ReservationProcess process, Instant now) {
        Optional<String> externalPatientId = userProfileRepositoryPort
            .findByUserId(process.getUserId())
            .map(profile -> profile.getExternalPatientId());

        if (externalPatientId.isEmpty()) {
            LOG.warn("No profile found for userId={}, expiring process {}", process.getUserId(), process.getExternalProcessId());
            expireProcess(process, now);
            return;
        }

        List<CatedraAppointmentClient.AppointmentItem> appointments;
        try {
            appointments = catedraAppointmentClient.listAppointmentsByPatient(externalPatientId.get());
        } catch (Exception e) {
            LOG.error("Failed to query catedra for process {}, will retry next cycle: {}", process.getExternalProcessId(), e.getMessage());
            return;
        }

        Optional<CatedraAppointmentClient.AppointmentItem> match = appointments
            .stream()
            .filter(item -> process.getExternalProcessId().equals(item.reservationProcessId()))
            .findFirst();

        if (match.isPresent() && "CONFIRMED".equals(match.get().status())) {
            LOG.info("Reconciling process {} as CONFIRMED (missed Kafka event)", process.getExternalProcessId());
            handleAppointmentConfirmedUseCase.handleConfirmed(process.getExternalProcessId(), match.get().reservationId());
        } else {
            LOG.info("Reconciling process {} as EXPIRED (not found or non-confirmed in catedra)", process.getExternalProcessId());
            expireProcess(process, now);
        }
    }

    private void expireProcess(ReservationProcess process, Instant now) {
        process.setStatus(ProcessStatus.EXPIRED);
        process.setUpdatedAt(now);
        reservationProcessRepositoryPort.save(process);
    }
}
