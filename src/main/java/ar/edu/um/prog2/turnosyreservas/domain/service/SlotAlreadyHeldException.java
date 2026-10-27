package ar.edu.um.prog2.turnosyreservas.domain.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SlotAlreadyHeldException extends RuntimeException {

    public SlotAlreadyHeldException(Long professionalId, LocalDate date, LocalTime startTime) {
        super("Slot already held for professional " + professionalId + " on " + date + " at " + startTime);
    }
}
