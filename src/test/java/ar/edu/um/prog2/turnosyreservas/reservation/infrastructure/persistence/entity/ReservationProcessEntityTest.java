package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity;

import static ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationProcessEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationProcessEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationProcessEntity.class);
        ReservationProcessEntity reservationProcess1 = getReservationProcessEntitySample1();
        ReservationProcessEntity reservationProcess2 = new ReservationProcessEntity();
        assertThat(reservationProcess1).isNotEqualTo(reservationProcess2);

        reservationProcess2.setId(reservationProcess1.getId());
        assertThat(reservationProcess1).isEqualTo(reservationProcess2);

        reservationProcess2 = getReservationProcessEntitySample2();
        assertThat(reservationProcess1).isNotEqualTo(reservationProcess2);
    }
}
