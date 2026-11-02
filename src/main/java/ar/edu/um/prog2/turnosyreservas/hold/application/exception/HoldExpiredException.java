package ar.edu.um.prog2.turnosyreservas.hold.application.exception;

public class HoldExpiredException extends RuntimeException {

    public HoldExpiredException(Long holdId) {
        super("Hold expired: " + holdId);
    }
}
