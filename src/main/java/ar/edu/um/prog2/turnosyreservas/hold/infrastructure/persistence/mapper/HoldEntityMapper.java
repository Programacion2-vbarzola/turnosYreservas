package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.mapper;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.Hold;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity.HoldEntity;
import org.springframework.stereotype.Component;

@Component
public class HoldEntityMapper {

    public Hold toDomain(HoldEntity entity) {
        if (entity == null) {
            return null;
        }
        Hold hold = new Hold();
        hold.setId(entity.getId());
        hold.setExternalHoldId(entity.getExternalHoldId());
        hold.setExternalProcessId(entity.getExternalProcessId());
        hold.setProfessionalId(entity.getProfessionalId());
        hold.setDate(entity.getDate());
        hold.setStartTime(entity.getStartTime());
        hold.setEndTime(entity.getEndTime());
        hold.setStatus(entity.getStatus());
        hold.setExpiresAt(entity.getExpiresAt());
        hold.setCreatedAt(entity.getCreatedAt());
        hold.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return hold;
    }

    public HoldEntity toEntity(Hold domain) {
        if (domain == null) {
            return null;
        }
        HoldEntity entity = new HoldEntity();
        entity.setId(domain.getId());
        entity.setExternalHoldId(domain.getExternalHoldId());
        entity.setExternalProcessId(domain.getExternalProcessId());
        entity.setProfessionalId(domain.getProfessionalId());
        entity.setDate(domain.getDate());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setStatus(domain.getStatus());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setCreatedAt(domain.getCreatedAt());
        // entity.user se resuelve en el adapter a partir de domain.getUserId()
        return entity;
    }
}
