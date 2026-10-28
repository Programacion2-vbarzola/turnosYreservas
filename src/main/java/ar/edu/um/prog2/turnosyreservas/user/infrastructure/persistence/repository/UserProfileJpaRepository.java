package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.repository;

import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserProfileEntity.
 */
@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long> {}
