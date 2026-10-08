package ar.edu.um.prog2.turnosyreservas.service.mapper;

import static ar.edu.um.prog2.turnosyreservas.domain.ReservationProcessAsserts.*;
import static ar.edu.um.prog2.turnosyreservas.domain.ReservationProcessTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationProcessMapperTest {

    private ReservationProcessMapper reservationProcessMapper;

    @BeforeEach
    void setUp() {
        reservationProcessMapper = new ReservationProcessMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReservationProcessSample1();
        var actual = reservationProcessMapper.toEntity(reservationProcessMapper.toDto(expected));
        assertReservationProcessAllPropertiesEquals(expected, actual);
    }
}
