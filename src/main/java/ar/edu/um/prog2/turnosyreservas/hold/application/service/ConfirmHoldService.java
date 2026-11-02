package ar.edu.um.prog2.turnosyreservas.hold.application.service;

import ar.edu.um.prog2.turnosyreservas.availability.domain.port.out.CatedraAppointmentClient;
import ar.edu.um.prog2.turnosyreservas.hold.application.exception.HoldExpiredException;
import ar.edu.um.prog2.turnosyreservas.hold.application.exception.HoldNotFoundException;
import ar.edu.um.prog2.turnosyreservas.hold.domain.model.Hold;
import ar.edu.um.prog2.turnosyreservas.hold.domain.model.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.hold.domain.port.in.ConfirmHoldUseCase;
import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.HoldRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
class ConfirmHoldService implements ConfirmHoldUseCase {

    private final HoldRepositoryPort holdRepositoryPort;
    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final CatedraAppointmentClient catedraAppointmentClient;

    ConfirmHoldService(
        HoldRepositoryPort holdRepositoryPort,
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        CatedraAppointmentClient catedraAppointmentClient
    ) {
        this.holdRepositoryPort = holdRepositoryPort;
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
        this.catedraAppointmentClient = catedraAppointmentClient;
    }

    @Override
    public ConfirmHoldResult confirmHold(Long holdId, String patientFirstName, String patientLastName, User user) {
        Hold hold = holdRepositoryPort.findById(holdId).orElseThrow(() -> new HoldNotFoundException(holdId));

        if (hold.getExpiresAt().isBefore(Instant.now())) {
            throw new HoldExpiredException(holdId);
        }

        CatedraAppointmentClient.ConfirmHoldResponse catedraResponse = catedraAppointmentClient.confirmHold(hold.getExternalHoldId());

        hold.setStatus(HoldStatus.CONFIRMED);
        holdRepositoryPort.save(hold);

        ReservationProcess process = reservationProcessRepositoryPort
            .findByHoldId(holdId)
            .orElseThrow(() -> new RuntimeException("ReservationProcess not found for hold: " + holdId));

        process.setPatientFirstName(patientFirstName);
        process.setPatientLastName(patientLastName);
        process.setStatus(ProcessStatus.WAITING_FOR_PHONE);
        process.setUpdatedAt(Instant.now());
        ReservationProcess savedProcess = reservationProcessRepositoryPort.save(process);

        return new ConfirmHoldResult(savedProcess.getId(), ProcessStatus.WAITING_FOR_PHONE.name(), catedraResponse.expiresAt());
    }
}
