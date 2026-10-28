package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.adapter;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.Hold;
import ar.edu.um.prog2.turnosyreservas.hold.domain.model.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.HoldRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity.HoldEntity;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.mapper.HoldEntityMapper;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.repository.HoldJpaRepository;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class HoldPersistenceAdapter implements HoldRepositoryPort {

    private final HoldJpaRepository holdJpaRepository;
    private final HoldEntityMapper holdEntityMapper;

    HoldPersistenceAdapter(HoldJpaRepository holdJpaRepository, HoldEntityMapper holdEntityMapper) {
        this.holdJpaRepository = holdJpaRepository;
        this.holdEntityMapper = holdEntityMapper;
    }

    @Override
    public Hold save(Hold hold) {
        HoldEntity entity = holdEntityMapper.toEntity(hold);
        UserEntity userRef = new UserEntity();
        userRef.setId(hold.getUserId());
        entity.setUser(userRef);
        HoldEntity saved = holdJpaRepository.save(entity);
        return holdEntityMapper.toDomain(saved);
    }

    @Override
    public boolean existsActiveHoldForSlot(Long professionalId, LocalDate date, LocalTime startTime) {
        return holdJpaRepository.existsByProfessionalIdAndDateAndStartTimeAndStatus(
            professionalId,
            date,
            startTime.toString(),
            HoldStatus.HELD
        );
    }
}
