package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.application.exception.ProcessNotFoundException;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ReservationStatus;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleAppointmentConfirmedUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationRepositoryPort;
import java.time.Instant;
import org.springframework.stereotype.Service;

//service para confirmar reserva y guardarla en BD local
@Service
class HandleAppointmentConfirmedService implements HandleAppointmentConfirmedUseCase {

    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;
    private final ReservationRepositoryPort reservationRepositoryPort;

    HandleAppointmentConfirmedService(
        ReservationProcessRepositoryPort reservationProcessRepositoryPort,
        ReservationRepositoryPort reservationRepositoryPort
    ) {
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    //Paso parametros externalProcessId proveniente al crear hold y externalreservationid creado al confirmar el turno
    @Override
    public void handleConfirmed(String externalProcessId, Long externalReservationId) {
        //Find external process id en bd local
        ReservationProcess process = reservationProcessRepositoryPort
            .findByExternalProcessId(externalProcessId)
            .orElseThrow(() -> new ProcessNotFoundException(externalProcessId));
        //COn el objeto que recupero con ese id de proceso guardo los datos de la reserva final
        Reservation reservation = new Reservation();
        reservation.setExternalReservationId(externalReservationId);
        reservation.setProfessionalId(process.getProfessionalId());
        reservation.setAppointmentDate(process.getDate());
        reservation.setStartTime(process.getStartTime());
        reservation.setPatientFirstName(process.getPatientFirstName());
        reservation.setPatientLastName(process.getPatientLastName());
        reservation.setPatientPhone(process.getPhoneNumber());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setCreatedAt(Instant.now());
        reservation.setConfirmedAt(Instant.now());
        reservation.setUserId(process.getUserId());
        reservation.setProcessId(process.getId());
        //Guardo en la base de datos
        Reservation savedReservation = reservationRepositoryPort.save(reservation);
        //Acutalizo tabla reservation process
        process.setStatus(ProcessStatus.CONFIRMED);
        process.setReservationId(savedReservation.getId());
        process.setUpdatedAt(Instant.now());
        reservationProcessRepositoryPort.save(process);
    }
}
