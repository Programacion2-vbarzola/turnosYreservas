package ar.edu.um.prog2.turnosyreservas.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;

public interface ReservationProcessRepositoryPort {
    ReservationProcess save(ReservationProcess reservationProcess);
}
