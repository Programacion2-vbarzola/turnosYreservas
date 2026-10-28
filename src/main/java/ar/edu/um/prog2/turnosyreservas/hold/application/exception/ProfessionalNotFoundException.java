package ar.edu.um.prog2.turnosyreservas.hold.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfessionalNotFoundException extends RuntimeException {

    public ProfessionalNotFoundException(Long professionalId) {
        super("Professional not found: " + professionalId);
    }
}
