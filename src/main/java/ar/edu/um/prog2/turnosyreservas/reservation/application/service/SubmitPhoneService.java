package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.Hold;
import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.AppointmentKafkaProducer;
import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.HoldRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.InvalidProcessStateException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessExpiredException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessOwnerMismatchException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.SubmitPhoneUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
class SubmitPhoneService implements SubmitPhoneUseCase {

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final HoldRepositoryPort holdRepositoryPort;
    private final AppointmentKafkaProducer appointmentKafkaProducer;

    SubmitPhoneService(
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        HoldRepositoryPort holdRepositoryPort,
        AppointmentKafkaProducer appointmentKafkaProducer
    ) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
        this.holdRepositoryPort = holdRepositoryPort;
        this.appointmentKafkaProducer = appointmentKafkaProducer;
    }

    @Override
    public PhoneSubmittedResult submitPhone(Long processId, String phoneNumber, User user) {
        ReservationProcess process = reservationProcessRepositoryPort
            .findById(processId)
            .orElseThrow(() -> new ProcessNotFoundException(processId));

        if (!process.getUserId().equals(user.getId())) {
            throw new ProcessOwnerMismatchException(processId);
        }

        if (process.getExpiresAt().isBefore(Instant.now())) {
            throw new ProcessExpiredException(process.getExternalProcessId());
        }

        if (process.getStatus() != ProcessStatus.PHONE_REQUESTED) {
            throw new InvalidProcessStateException(process.getExternalProcessId(), process.getStatus().name());
        }

        Instant now = Instant.now();
        process.setPhoneNumber(phoneNumber);
        process.setStatus(ProcessStatus.PHONE_SUBMITTED);
        process.setExpiresAt(now.plus(10, ChronoUnit.MINUTES));
        process.setUpdatedAt(now);
        reservationProcessRepositoryPort.save(process);

        Hold hold = holdRepositoryPort
            .findById(process.getHoldId())
            .orElseThrow(() -> new IllegalStateException("Hold not found for process: " + processId));

        appointmentKafkaProducer.sendPhone(
            process.getExternalProcessId(),
            hold.getExternalHoldId(),
            process.getRequestEventId(),
            phoneNumber
        );

        return new PhoneSubmittedResult(process.getId(), ProcessStatus.PHONE_SUBMITTED.name());
    }
}
