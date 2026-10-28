package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;

public interface ReservationProcessRepositoryPort {
    ReservationProcess save(ReservationProcess reservationProcess);
}
