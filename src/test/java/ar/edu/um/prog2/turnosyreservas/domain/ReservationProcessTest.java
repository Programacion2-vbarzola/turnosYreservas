package ar.edu.um.prog2.turnosyreservas.domain;

import static ar.edu.um.prog2.turnosyreservas.domain.HoldTestSamples.*;
import static ar.edu.um.prog2.turnosyreservas.domain.ReservationProcessTestSamples.*;
import static ar.edu.um.prog2.turnosyreservas.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationProcessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationProcess.class);
        ReservationProcess reservationProcess1 = getReservationProcessSample1();
        ReservationProcess reservationProcess2 = new ReservationProcess();
        assertThat(reservationProcess1).isNotEqualTo(reservationProcess2);

        reservationProcess2.setId(reservationProcess1.getId());
        assertThat(reservationProcess1).isEqualTo(reservationProcess2);

        reservationProcess2 = getReservationProcessSample2();
        assertThat(reservationProcess1).isNotEqualTo(reservationProcess2);
    }

    @Test
    void holdTest() {
        ReservationProcess reservationProcess = getReservationProcessRandomSampleGenerator();
        Hold holdBack = getHoldRandomSampleGenerator();

        reservationProcess.setHold(holdBack);
        assertThat(reservationProcess.getHold()).isEqualTo(holdBack);

        reservationProcess.hold(null);
        assertThat(reservationProcess.getHold()).isNull();
    }

    @Test
    void reservationTest() {
        ReservationProcess reservationProcess = getReservationProcessRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        reservationProcess.setReservation(reservationBack);
        assertThat(reservationProcess.getReservation()).isEqualTo(reservationBack);
        assertThat(reservationBack.getProcess()).isEqualTo(reservationProcess);

        reservationProcess.reservation(null);
        assertThat(reservationProcess.getReservation()).isNull();
        assertThat(reservationBack.getProcess()).isNull();
    }
}
