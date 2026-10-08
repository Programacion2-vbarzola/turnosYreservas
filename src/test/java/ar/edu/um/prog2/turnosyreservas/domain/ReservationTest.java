package ar.edu.um.prog2.turnosyreservas.domain;

import static ar.edu.um.prog2.turnosyreservas.domain.ReservationProcessTestSamples.*;
import static ar.edu.um.prog2.turnosyreservas.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void processTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        ReservationProcess reservationProcessBack = getReservationProcessRandomSampleGenerator();

        reservation.setProcess(reservationProcessBack);
        assertThat(reservation.getProcess()).isEqualTo(reservationProcessBack);

        reservation.process(null);
        assertThat(reservation.getProcess()).isNull();
    }
}
