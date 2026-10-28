package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.adapter;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.UserProfile;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserProfileRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserProfileEntity;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.mapper.UserProfileEntityMapper;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.repository.UserProfileJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class UserProfilePersistenceAdapter implements UserProfileRepositoryPort {

    private final UserProfileJpaRepository userProfileJpaRepository;
    private final UserProfileEntityMapper userProfileEntityMapper;

    UserProfilePersistenceAdapter(UserProfileJpaRepository userProfileJpaRepository, UserProfileEntityMapper userProfileEntityMapper) {
        this.userProfileJpaRepository = userProfileJpaRepository;
        this.userProfileEntityMapper = userProfileEntityMapper;
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity entity = userProfileEntityMapper.toEntity(userProfile);
        UserEntity userRef = new UserEntity();
        userRef.setId(userProfile.getUserId());
        entity.setUser(userRef);
        UserProfileEntity saved = userProfileJpaRepository.save(entity);
        return userProfileEntityMapper.toDomain(saved);
    }
}
