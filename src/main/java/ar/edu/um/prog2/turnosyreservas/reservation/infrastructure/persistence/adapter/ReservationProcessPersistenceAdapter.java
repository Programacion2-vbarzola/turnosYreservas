package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.adapter;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationProcessRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationProcessEntity;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.mapper.ReservationProcessEntityMapper;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.repository.ReservationProcessJpaRepository;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationProcessPersistenceAdapter implements ReservationProcessRepositoryPort {

    private final ReservationProcessJpaRepository reservationProcessJpaRepository;
    private final ReservationProcessEntityMapper reservationProcessEntityMapper;

    ReservationProcessPersistenceAdapter(
        ReservationProcessJpaRepository reservationProcessJpaRepository,
        ReservationProcessEntityMapper reservationProcessEntityMapper
    ) {
        this.reservationProcessJpaRepository = reservationProcessJpaRepository;
        this.reservationProcessEntityMapper = reservationProcessEntityMapper;
    }

    @Override
    public ReservationProcess save(ReservationProcess reservationProcess) {
        ReservationProcessEntity entity = reservationProcessEntityMapper.toEntity(reservationProcess);
        UserEntity userRef = new UserEntity();
        userRef.setId(reservationProcess.getUserId());
        entity.setUser(userRef);
        ReservationProcessEntity saved = reservationProcessJpaRepository.save(entity);
        return reservationProcessEntityMapper.toDomain(saved);
    }
}
