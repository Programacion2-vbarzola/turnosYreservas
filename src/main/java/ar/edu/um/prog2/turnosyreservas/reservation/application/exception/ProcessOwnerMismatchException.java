package ar.edu.um.prog2.turnosyreservas.reservation.application.exception;

public class ProcessOwnerMismatchException extends RuntimeException {

    public ProcessOwnerMismatchException(Long processId) {
        super("Process does not belong to current user: " + processId);
    }
}
