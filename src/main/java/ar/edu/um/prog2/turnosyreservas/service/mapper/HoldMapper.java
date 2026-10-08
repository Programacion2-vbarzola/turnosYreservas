package ar.edu.um.prog2.turnosyreservas.service.mapper;

import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.domain.User;
import ar.edu.um.prog2.turnosyreservas.service.dto.HoldDTO;
import ar.edu.um.prog2.turnosyreservas.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hold} and its DTO {@link HoldDTO}.
 */
@Mapper(componentModel = "spring")
public interface HoldMapper extends EntityMapper<HoldDTO, Hold> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    HoldDTO toDto(Hold s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
