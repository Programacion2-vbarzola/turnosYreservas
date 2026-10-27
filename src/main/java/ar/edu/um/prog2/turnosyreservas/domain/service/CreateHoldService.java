package ar.edu.um.prog2.turnosyreservas.domain.service;

import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.domain.User;
import ar.edu.um.prog2.turnosyreservas.domain.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.domain.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.domain.port.in.CreateHoldUseCase;
import ar.edu.um.prog2.turnosyreservas.domain.port.in.CreateHoldUseCase;
import ar.edu.um.prog2.turnosyreservas.domain.port.out.CatalogServiceClient;
import ar.edu.um.prog2.turnosyreservas.domain.port.out.CatedraAppointmentClient;
import ar.edu.um.prog2.turnosyreservas.domain.port.out.HoldRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.domain.port.out.ReservationProcessRepositoryPort;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

@Service
class CreateHoldService implements CreateHoldUseCase {

    private final CatalogServiceClient catalogServiceClient;
    private final CatedraAppointmentClient catedraAppointmentClient;
    private final HoldRepositoryPort holdRepositoryPort;
    private final ReservationProcessRepositoryPort reservationProcessRepositoryPort;

    //constructor
    CreateHoldService(
        CatalogServiceClient catalogServiceClient,
        CatedraAppointmentClient catedraAppointmentClient,
        HoldRepositoryPort holdRepositoryPort,
        ReservationProcessRepositoryPort reservationProcessRepositoryPort
    ) {
        this.catalogServiceClient = catalogServiceClient;
        this.catedraAppointmentClient = catedraAppointmentClient;
        this.holdRepositoryPort = holdRepositoryPort;
        this.reservationProcessRepositoryPort = reservationProcessRepositoryPort;
    }

    //metodo para pedir el hold y validaciones de existencia de hold previo
    @Override
    public HoldCreatedResult createHold(Long professionalId, LocalDate date, LocalTime startTime, User user, String externalPatientId) {
        //sino encunetra arrojar not found profesional. valida si existe el profesional
        catalogServiceClient.getProfessional(professionalId).orElseThrow(() -> new ProfessionalNotFoundException(professionalId));
        //si ya habia un hold en bd local para esos datos arrojar exception
        if (holdRepositoryPort.existsActiveHoldForSlot(professionalId, date, startTime)) {
            throw new SlotAlreadyHeldException(professionalId, date, startTime);
        }
        //comunica a catedra service para crear el hold
        CatedraAppointmentClient.HoldResponse holdResponse = catedraAppointmentClient.createHold(
            professionalId,
            date,
            startTime,
            externalPatientId
        );
        //guardado del hold en la base local + conversion de startTime a ToString() para bd
        Hold hold = new Hold();
        hold.setProfessionalId(professionalId);
        hold.setDate(date);
        hold.setStartTime(startTime.toString());
        hold.setStatus(HoldStatus.HELD);
        hold.setExternalHoldId(holdResponse.externalHoldId());
        hold.setExternalProcessId(holdResponse.externalProcessId());
        hold.setExpiresAt(holdResponse.expiresAt());
        hold.setCreatedAt(Instant.now());
        hold.setUser(user);

        Hold savedHold = holdRepositoryPort.save(hold);
        // instancio reservationProcess y guardamos la reserva en la base de datos local para inciar proceso de reservacion de turno
        ReservationProcess process = new ReservationProcess();
        process.setExternalProcessId(holdResponse.externalProcessId());
        process.setProfessionalId(professionalId);
        process.setDate(date);
        process.setStartTime(startTime.toString());
        process.setStatus(ProcessStatus.HELD);
        process.setExpiresAt(holdResponse.expiresAt());
        process.setPatientFirstName(user.getFirstName());
        process.setPatientLastName(user.getLastName());
        process.setCreatedAt(Instant.now());
        process.setUpdatedAt(Instant.now());
        process.setHold(savedHold);
        process.setUser(user);

        ReservationProcess savedProcess = reservationProcessRepositoryPort.save(process);
        //return de hold creado y guardado con parametros
        return new HoldCreatedResult(savedHold.getId(), savedProcess.getId(), HoldStatus.HELD.name(), savedHold.getExpiresAt());
    }
}
