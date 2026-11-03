package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.AppointmentKafkaProducer;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessExpiredException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessOwnerMismatchException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.SubmitPhoneUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
class SubmitPhoneService implements SubmitPhoneUseCase {

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final AppointmentKafkaProducer appointmentKafkaProducer;

    SubmitPhoneService(
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        AppointmentKafkaProducer appointmentKafkaProducer
    ) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
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

        process.setPhoneNumber(phoneNumber);
        process.setStatus(ProcessStatus.PHONE_SUBMITTED);
        process.setUpdatedAt(Instant.now());
        reservationProcessRepositoryPort.save(process);

        appointmentKafkaProducer.sendPhone(process.getExternalProcessId(), phoneNumber);

        return new PhoneSubmittedResult(process.getId(), ProcessStatus.PHONE_SUBMITTED.name());
    }
}
