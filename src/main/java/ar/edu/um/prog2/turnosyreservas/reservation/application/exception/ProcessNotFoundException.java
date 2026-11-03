package ar.edu.um.prog2.turnosyreservas.reservation.application.exception;

public class ProcessNotFoundException extends RuntimeException {

    public ProcessNotFoundException(Long processId) {
        super("Process not found: " + processId);
    }

    public ProcessNotFoundException(String externalProcessId) {
        super("Process not found for external id: " + externalProcessId);
    }
}
