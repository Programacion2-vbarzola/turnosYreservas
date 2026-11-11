package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.adapter;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationEntity;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationProcessEntity;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.mapper.ReservationEntityMapper;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.repository.ReservationJpaRepository;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ReservationPersistenceAdapter implements ReservationRepositoryPort {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationEntityMapper reservationEntityMapper;

    ReservationPersistenceAdapter(ReservationJpaRepository reservationJpaRepository, ReservationEntityMapper reservationEntityMapper) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.reservationEntityMapper = reservationEntityMapper;
    }

    @Override
    //metodo para guardar en bd la reserva con puereto de salida
    public Reservation save(Reservation reservation) {
        //Transform modelo de dominio "reservation" a entidad de dominio
        ReservationEntity entity = reservationEntityMapper.toEntity(reservation);

        //Establlesco la relacion Entre reservationProcess y Reservation seteando process id y user. SetProcess Seimrpe espera objeto tipo ReservationProcessEntity
        ReservationProcessEntity processRef = new ReservationProcessEntity();
        processRef.setId(reservation.getProcessId());
        entity.setProcess(processRef);

        UserEntity userRef = new UserEntity();
        userRef.setId(reservation.getUserId());
        entity.setUser(userRef);

        //Guardo reserva  en BD usando puesto de salida
        ReservationEntity saved = reservationJpaRepository.save(entity);

        //Transformo reservationEntity a dominio y devuelve al service
        return reservationEntityMapper.toDomain(saved);
    }

    @Override
    //metodo que usa interfaz de JpaReservationRepositery para buscar reservations por userID
    public List<Reservation> findByUserId(Long userId) {
        return reservationJpaRepository.findByUserId(userId).stream().map(reservationEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Reservation> findByProcessId(Long processId) {
        return reservationJpaRepository.findByProcessId(processId).map(reservationEntityMapper::toDomain);
    }
}
