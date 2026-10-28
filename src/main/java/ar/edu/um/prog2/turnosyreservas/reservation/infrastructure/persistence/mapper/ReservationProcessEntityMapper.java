package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.mapper;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationProcessEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationProcessEntityMapper {

    public ReservationProcess toDomain(ReservationProcessEntity entity) {
        if (entity == null) {
            return null;
        }
        ReservationProcess process = new ReservationProcess();
        process.setId(entity.getId());
        process.setExternalProcessId(entity.getExternalProcessId());
        process.setProfessionalId(entity.getProfessionalId());
        process.setDate(entity.getDate());
        process.setStartTime(entity.getStartTime());
        process.setPatientFirstName(entity.getPatientFirstName());
        process.setPatientLastName(entity.getPatientLastName());
        process.setStatus(entity.getStatus());
        process.setExpiresAt(entity.getExpiresAt());
        process.setRequestEventId(entity.getRequestEventId());
        process.setPhoneNumber(entity.getPhoneNumber());
        process.setCreatedAt(entity.getCreatedAt());
        process.setUpdatedAt(entity.getUpdatedAt());
        process.setHoldId(entity.getHoldId());
        process.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return process;
    }

    public ReservationProcessEntity toEntity(ReservationProcess domain) {
        if (domain == null) {
            return null;
        }
        ReservationProcessEntity entity = new ReservationProcessEntity();
        entity.setId(domain.getId());
        entity.setExternalProcessId(domain.getExternalProcessId());
        entity.setProfessionalId(domain.getProfessionalId());
        entity.setDate(domain.getDate());
        entity.setStartTime(domain.getStartTime());
        entity.setPatientFirstName(domain.getPatientFirstName());
        entity.setPatientLastName(domain.getPatientLastName());
        entity.setStatus(domain.getStatus());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setRequestEventId(domain.getRequestEventId());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setHoldId(domain.getHoldId());
        // entity.user se resuelve en el adapter a partir de domain.getUserId()
        return entity;
    }
}
