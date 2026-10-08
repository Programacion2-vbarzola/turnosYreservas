package ar.edu.um.prog2.turnosyreservas.domain;

import static ar.edu.um.prog2.turnosyreservas.domain.HoldTestSamples.*;
import static ar.edu.um.prog2.turnosyreservas.domain.ReservationProcessTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hold.class);
        Hold hold1 = getHoldSample1();
        Hold hold2 = new Hold();
        assertThat(hold1).isNotEqualTo(hold2);

        hold2.setId(hold1.getId());
        assertThat(hold1).isEqualTo(hold2);

        hold2 = getHoldSample2();
        assertThat(hold1).isNotEqualTo(hold2);
    }

    @Test
    void reservationProcessTest() {
        Hold hold = getHoldRandomSampleGenerator();
        ReservationProcess reservationProcessBack = getReservationProcessRandomSampleGenerator();

        hold.setReservationProcess(reservationProcessBack);
        assertThat(hold.getReservationProcess()).isEqualTo(reservationProcessBack);
        assertThat(reservationProcessBack.getHold()).isEqualTo(hold);

        hold.reservationProcess(null);
        assertThat(hold.getReservationProcess()).isNull();
        assertThat(reservationProcessBack.getHold()).isNull();
    }
}
