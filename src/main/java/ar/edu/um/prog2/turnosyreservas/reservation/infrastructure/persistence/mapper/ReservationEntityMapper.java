package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.mapper;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationEntityMapper {

    public Reservation toDomain(ReservationEntity entity) {
        if (entity == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(entity.getId());
        reservation.setExternalReservationId(entity.getExternalReservationId());
        reservation.setProfessionalId(entity.getProfessionalId());
        reservation.setProfessionalFirstName(entity.getProfessionalFirstName());
        reservation.setProfessionalLastName(entity.getProfessionalLastName());
        reservation.setAppointmentDate(entity.getAppointmentDate());
        reservation.setStartTime(entity.getStartTime());
        reservation.setEndTime(entity.getEndTime());
        reservation.setPatientFirstName(entity.getPatientFirstName());
        reservation.setPatientLastName(entity.getPatientLastName());
        reservation.setPatientPhone(entity.getPatientPhone());
        reservation.setStatus(entity.getStatus());
        reservation.setCreatedAt(entity.getCreatedAt());
        reservation.setConfirmedAt(entity.getConfirmedAt());
        reservation.setCancelledAt(entity.getCancelledAt());
        reservation.setCancellationReason(entity.getCancellationReason());
        reservation.setProcessId(entity.getProcess() != null ? entity.getProcess().getId() : null);
        reservation.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return reservation;
    }

    public ReservationEntity toEntity(Reservation domain) {
        if (domain == null) {
            return null;
        }
        ReservationEntity entity = new ReservationEntity();
        entity.setId(domain.getId());
        entity.setExternalReservationId(domain.getExternalReservationId());
        entity.setProfessionalId(domain.getProfessionalId());
        entity.setProfessionalFirstName(domain.getProfessionalFirstName());
        entity.setProfessionalLastName(domain.getProfessionalLastName());
        entity.setAppointmentDate(domain.getAppointmentDate());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setPatientFirstName(domain.getPatientFirstName());
        entity.setPatientLastName(domain.getPatientLastName());
        entity.setPatientPhone(domain.getPatientPhone());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setConfirmedAt(domain.getConfirmedAt());
        entity.setCancelledAt(domain.getCancelledAt());
        entity.setCancellationReason(domain.getCancellationReason());
        // entity.process y entity.user se resuelven en el adapter a partir de los IDs del domain
        return entity;
    }
}
