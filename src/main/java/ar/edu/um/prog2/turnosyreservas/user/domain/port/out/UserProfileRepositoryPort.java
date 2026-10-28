package ar.edu.um.prog2.turnosyreservas.user.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.UserProfile;

public interface UserProfileRepositoryPort {
    UserProfile save(UserProfile userProfile);
}
