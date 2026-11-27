package ar.edu.um.prog2.turnosyreservas.reservation.application.exception;

public class InvalidProcessStateException extends RuntimeException {

    public InvalidProcessStateException(String processId, String currentStatus) {
        super("Process " + processId + " is in invalid state for this operation: " + currentStatus);
    }
}
