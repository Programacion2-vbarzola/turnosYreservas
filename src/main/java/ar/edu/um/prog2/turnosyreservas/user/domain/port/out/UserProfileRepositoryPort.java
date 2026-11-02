package ar.edu.um.prog2.turnosyreservas.user.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.UserProfile;
import java.util.Optional;

public interface UserProfileRepositoryPort {
    UserProfile save(UserProfile userProfile);
    Optional<UserProfile> findByUserId(Long userId);
}
