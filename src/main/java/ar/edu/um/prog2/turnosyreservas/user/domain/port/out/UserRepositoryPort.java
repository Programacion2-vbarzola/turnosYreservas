package ar.edu.um.prog2.turnosyreservas.user.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryPort {
    Optional<User> findOneByActivationKey(String activationKey);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);
    Optional<User> findOneWithAuthoritiesByLogin(String login);
    Optional<User> findById(Long id);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
    User save(User user);
    void delete(User user);
    Optional<String> findAuthorityByName(String name);
    List<String> findAllAuthorityNames();
}
