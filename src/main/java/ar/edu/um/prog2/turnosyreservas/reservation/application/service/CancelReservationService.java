package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.availability.domain.port.out.CatedraAppointmentClient;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessOwnerMismatchException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ReservationStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.CancelReservationUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
class CancelReservationService implements CancelReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final CatedraAppointmentClient catedraAppointmentClient;

    CancelReservationService(
        ReservationRepositoryPort reservationRepositoryPort,
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        CatedraAppointmentClient catedraAppointmentClient
    ) {
        this.reservationRepositoryPort = reservationRepositoryPort;
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
        this.catedraAppointmentClient = catedraAppointmentClient;
    }

    @Override
    public CancelReservationResult cancelReservation(Long reservationProcessId, String reason, User user) {
        ReservationProcess process = reservationProcessRepositoryPort
            .findById(reservationProcessId)
            .orElseThrow(() -> new ProcessNotFoundException(reservationProcessId));

        if (!process.getUserId().equals(user.getId())) {
            throw new ProcessOwnerMismatchException(reservationProcessId);
        }

        Reservation reservation = reservationRepositoryPort
            .findByProcessId(reservationProcessId)
            .orElseThrow(() -> new ProcessNotFoundException(reservationProcessId));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return new CancelReservationResult(reservationProcessId, ReservationStatus.CANCELLED.name(), reservation.getCancelledAt());
        }

        catedraAppointmentClient.cancelAppointment(process.getExternalProcessId(), reason);

        Instant now = Instant.now();

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(now);
        reservation.setCancellationReason(reason);
        reservationRepositoryPort.save(reservation);

        process.setStatus(ProcessStatus.CANCELLED);
        process.setUpdatedAt(now);
        reservationProcessRepositoryPort.save(process);

        return new CancelReservationResult(reservationProcessId, ReservationStatus.CANCELLED.name(), now);
    }
}
