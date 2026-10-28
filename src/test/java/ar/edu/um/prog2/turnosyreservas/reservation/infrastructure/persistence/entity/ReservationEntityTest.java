package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity;

import static ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationEntityTestSamples.*;
import static ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationProcessEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationEntity.class);
        ReservationEntity reservation1 = getReservationEntitySample1();
        ReservationEntity reservation2 = new ReservationEntity();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationEntitySample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void processTest() {
        ReservationEntity reservation = getReservationEntityRandomSampleGenerator();
        ReservationProcessEntity processBack = getReservationProcessEntityRandomSampleGenerator();

        reservation.setProcess(processBack);
        assertThat(reservation.getProcess()).isEqualTo(processBack);

        reservation.setProcess(null);
        assertThat(reservation.getProcess()).isNull();
    }
}
