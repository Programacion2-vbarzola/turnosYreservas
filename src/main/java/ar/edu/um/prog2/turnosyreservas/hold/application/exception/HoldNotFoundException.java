package ar.edu.um.prog2.turnosyreservas.hold.application.exception;

public class HoldNotFoundException extends RuntimeException {

    public HoldNotFoundException(Long holdId) {
        super("Hold not found: " + holdId);
    }
}
