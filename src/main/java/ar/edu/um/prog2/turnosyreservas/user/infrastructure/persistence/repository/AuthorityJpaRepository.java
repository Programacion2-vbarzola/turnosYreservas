package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.repository;

import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AuthorityEntity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityJpaRepository extends JpaRepository<AuthorityEntity, String> {}
