package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.mapper;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.AuthorityEntity;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        user.setId(entity.getId());
        user.setLogin(entity.getLogin());
        user.setPassword(entity.getPassword());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setActivated(entity.isActivated());
        user.setLangKey(entity.getLangKey());
        user.setImageUrl(entity.getImageUrl());
        user.setActivationKey(entity.getActivationKey());
        user.setResetKey(entity.getResetKey());
        user.setResetDate(entity.getResetDate());
        user.setAuthorities(entity.getAuthorities().stream().map(AuthorityEntity::getName).collect(Collectors.toSet()));
        user.setCreatedBy(entity.getCreatedBy());
        user.setCreatedDate(entity.getCreatedDate());
        user.setLastModifiedBy(entity.getLastModifiedBy());
        user.setLastModifiedDate(entity.getLastModifiedDate());
        return user;
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setActivated(domain.isActivated());
        entity.setLangKey(domain.getLangKey());
        entity.setImageUrl(domain.getImageUrl());
        entity.setActivationKey(domain.getActivationKey());
        entity.setResetKey(domain.getResetKey());
        entity.setResetDate(domain.getResetDate());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setCreatedDate(domain.getCreatedDate());
        entity.setLastModifiedBy(domain.getLastModifiedBy());
        entity.setLastModifiedDate(domain.getLastModifiedDate());
        // entity.authorities se resuelve en el adapter a partir de domain.getAuthorities()
        return entity;
    }

    public Set<String> toAuthorityNames(Set<AuthorityEntity> authorities) {
        return authorities.stream().map(AuthorityEntity::getName).collect(Collectors.toSet());
    }
}
