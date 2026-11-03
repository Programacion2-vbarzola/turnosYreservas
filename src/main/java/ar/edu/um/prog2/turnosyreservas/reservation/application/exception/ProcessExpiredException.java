package ar.edu.um.prog2.turnosyreservas.reservation.application.exception;

public class ProcessExpiredException extends RuntimeException {

    public ProcessExpiredException(String externalProcessId) {
        super("Process expired: " + externalProcessId);
    }
}
