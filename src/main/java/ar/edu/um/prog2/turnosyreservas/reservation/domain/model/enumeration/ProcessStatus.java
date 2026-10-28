package ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration;

/**
 * The ProcessStatus enumeration.
 */
public enum ProcessStatus {
    HELD,
    WAITING_FOR_PHONE,
    PHONE_REQUESTED,
    PHONE_SUBMITTED,
    CONFIRMED,
    REJECTED,
    EXPIRED,
    INVALID,
    CANCELLED,
    FAILED,
}
