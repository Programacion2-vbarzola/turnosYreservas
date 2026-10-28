package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.adapter;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.AuthorityEntity;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.mapper.UserEntityMapper;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.repository.AuthorityJpaRepository;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.repository.UserJpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final AuthorityJpaRepository authorityJpaRepository;
    private final UserEntityMapper userEntityMapper;
    private final CacheManager cacheManager;

    UserPersistenceAdapter(
        UserJpaRepository userJpaRepository,
        AuthorityJpaRepository authorityJpaRepository,
        UserEntityMapper userEntityMapper,
        CacheManager cacheManager
    ) {
        this.userJpaRepository = userJpaRepository;
        this.authorityJpaRepository = authorityJpaRepository;
        this.userEntityMapper = userEntityMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    public Optional<User> findOneByActivationKey(String activationKey) {
        return userJpaRepository.findOneByActivationKey(activationKey).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findOneByResetKey(String resetKey) {
        return userJpaRepository.findOneByResetKey(resetKey).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findOneByEmailIgnoreCase(String email) {
        return userJpaRepository.findOneByEmailIgnoreCase(email).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findOneByLogin(String login) {
        return userJpaRepository.findOneByLogin(login).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findOneWithAuthoritiesByLogin(String login) {
        return userJpaRepository.findOneWithAuthoritiesByLogin(login).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userEntityMapper::toDomain);
    }

    @Override
    public List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime) {
        return userJpaRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(dateTime)
            .stream()
            .map(userEntityMapper::toDomain)
            .toList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userJpaRepository.findAll(pageable).map(userEntityMapper::toDomain);
    }

    @Override
    public Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable) {
        return userJpaRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(userEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userEntityMapper.toEntity(user);
        Set<AuthorityEntity> authorities = user
            .getAuthorities()
            .stream()
            .map(name -> {
                AuthorityEntity authority = new AuthorityEntity();
                authority.setName(name);
                return authority;
            })
            .collect(Collectors.toSet());
        entity.setAuthorities(authorities);
        UserEntity saved = userJpaRepository.save(entity);
        clearUserCaches(saved);
        return userEntityMapper.toDomain(saved);
    }

    @Override
    public void delete(User user) {
        userJpaRepository
            .findById(user.getId())
            .ifPresent(entity -> {
                userJpaRepository.delete(entity);
                userJpaRepository.flush();
                clearUserCaches(entity);
            });
    }

    @Override
    public Optional<String> findAuthorityByName(String name) {
        return authorityJpaRepository.findById(name).map(AuthorityEntity::getName);
    }

    @Override
    public List<String> findAllAuthorityNames() {
        return authorityJpaRepository.findAll().stream().map(AuthorityEntity::getName).toList();
    }

    private void clearUserCaches(UserEntity user) {
        Objects.requireNonNull(cacheManager.getCache(UserJpaRepository.USERS_BY_LOGIN_CACHE)).evictIfPresent(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserJpaRepository.USERS_BY_EMAIL_CACHE)).evictIfPresent(user.getEmail());
        }
    }
}
