package ar.edu.um.prog2.turnosyreservas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservationProcessDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationProcessDTO.class);
        ReservationProcessDTO reservationProcessDTO1 = new ReservationProcessDTO();
        reservationProcessDTO1.setId(1L);
        ReservationProcessDTO reservationProcessDTO2 = new ReservationProcessDTO();
        assertThat(reservationProcessDTO1).isNotEqualTo(reservationProcessDTO2);
        reservationProcessDTO2.setId(reservationProcessDTO1.getId());
        assertThat(reservationProcessDTO1).isEqualTo(reservationProcessDTO2);
        reservationProcessDTO2.setId(2L);
        assertThat(reservationProcessDTO1).isNotEqualTo(reservationProcessDTO2);
        reservationProcessDTO1.setId(null);
        assertThat(reservationProcessDTO1).isNotEqualTo(reservationProcessDTO2);
    }
}
