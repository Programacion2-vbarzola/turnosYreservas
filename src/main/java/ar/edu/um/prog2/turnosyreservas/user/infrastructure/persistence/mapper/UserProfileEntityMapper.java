package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.mapper;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.UserProfile;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class UserProfileEntityMapper {

    public UserProfile toDomain(UserProfileEntity entity) {
        if (entity == null) {
            return null;
        }
        UserProfile profile = new UserProfile();
        profile.setId(entity.getId());
        profile.setExternalPatientId(entity.getExternalPatientId());
        profile.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return profile;
    }

    public UserProfileEntity toEntity(UserProfile domain) {
        if (domain == null) {
            return null;
        }
        UserProfileEntity entity = new UserProfileEntity();
        entity.setId(domain.getId());
        entity.setExternalPatientId(domain.getExternalPatientId());
        // entity.user se resuelve en el adapter a partir de domain.getUserId()
        return entity;
    }
}
