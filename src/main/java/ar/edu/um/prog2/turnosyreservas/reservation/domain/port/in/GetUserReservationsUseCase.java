package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import java.util.List;

public interface GetUserReservationsUseCase {
    List<Reservation> getUserReservations(Long userId);
}
