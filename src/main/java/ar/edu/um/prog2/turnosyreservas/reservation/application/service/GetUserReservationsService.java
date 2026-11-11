package ar.edu.um.prog2.turnosyreservas.reservation.application.service;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.GetUserReservationsUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ReservationRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
class GetUserReservationsService implements GetUserReservationsUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;

    GetUserReservationsService(ReservationRepositoryPort reservationRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    //Metodo para buscar una lista de reservations asociada a un user, por userId
    @Override
    public List<Reservation> getUserReservations(Long userId) {
        return reservationRepositoryPort.findByUserId(userId);
    }
}
