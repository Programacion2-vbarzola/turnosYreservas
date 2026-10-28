package ar.edu.um.prog2.turnosyreservas.availability.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.availability.domain.model.AvailabilitySlot;
import java.time.LocalDate;
import java.util.List;

public interface GetAvailabilityUseCase {
    /**
     * Returns all slots for the given professional and date, each marked as available or not.
     * Throws ProfessionalNotFoundException (→ 404) if the professional does not exist in Repo 2.
     */
    List<AvailabilitySlot> getAvailability(Long professionalId, LocalDate date);
}
