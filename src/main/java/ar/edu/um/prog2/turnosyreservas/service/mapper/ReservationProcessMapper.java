package ar.edu.um.prog2.turnosyreservas.service.mapper;

import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.domain.User;
import ar.edu.um.prog2.turnosyreservas.service.dto.HoldDTO;
import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationProcessDTO;
import ar.edu.um.prog2.turnosyreservas.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReservationProcess} and its DTO {@link ReservationProcessDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationProcessMapper extends EntityMapper<ReservationProcessDTO, ReservationProcess> {
    @Mapping(target = "hold", source = "hold", qualifiedByName = "holdId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ReservationProcessDTO toDto(ReservationProcess s);

    @Named("holdId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HoldDTO toDtoHoldId(Hold hold);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
