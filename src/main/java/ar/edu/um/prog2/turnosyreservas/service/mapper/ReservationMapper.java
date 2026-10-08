package ar.edu.um.prog2.turnosyreservas.service.mapper;

import ar.edu.um.prog2.turnosyreservas.domain.Reservation;
import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.domain.User;
import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationDTO;
import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationProcessDTO;
import ar.edu.um.prog2.turnosyreservas.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "process", source = "process", qualifiedByName = "reservationProcessId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ReservationDTO toDto(Reservation s);

    @Named("reservationProcessId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReservationProcessDTO toDtoReservationProcessId(ReservationProcess reservationProcess);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
