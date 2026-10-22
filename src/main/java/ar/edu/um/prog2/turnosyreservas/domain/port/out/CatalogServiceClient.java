package ar.edu.um.prog2.turnosyreservas.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.domain.model.ProfessionalInfo;
import ar.edu.um.prog2.turnosyreservas.domain.model.ScheduleSlot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CatalogServiceClient {
    Optional<ProfessionalInfo> getProfessional(Long professionalId);
    List<ScheduleSlot> getSlotsForDate(Long professionalId, LocalDate date);
}
