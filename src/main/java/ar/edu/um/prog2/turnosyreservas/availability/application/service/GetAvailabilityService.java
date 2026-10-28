package ar.edu.um.prog2.turnosyreservas.availability.application.service;

import ar.edu.um.prog2.turnosyreservas.availability.domain.model.AvailabilitySlot;
import ar.edu.um.prog2.turnosyreservas.availability.domain.model.Occupancy;
import ar.edu.um.prog2.turnosyreservas.availability.domain.port.in.GetAvailabilityUseCase;
import ar.edu.um.prog2.turnosyreservas.availability.domain.port.out.CatalogServiceClient;
import ar.edu.um.prog2.turnosyreservas.availability.domain.port.out.CatedraAppointmentClient;
import ar.edu.um.prog2.turnosyreservas.hold.application.exception.ProfessionalNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
class GetAvailabilityService implements GetAvailabilityUseCase {

    private final CatalogServiceClient catalogServiceClient;
    private final CatedraAppointmentClient catedraAppointmentClient;

    GetAvailabilityService(CatalogServiceClient catalogServiceClient, CatedraAppointmentClient catedraAppointmentClient) {
        this.catalogServiceClient = catalogServiceClient;
        this.catedraAppointmentClient = catedraAppointmentClient;
    }

    @Override
    public List<AvailabilitySlot> getAvailability(Long professionalId, LocalDate date) {
        catalogServiceClient.getProfessional(professionalId).orElseThrow(() -> new ProfessionalNotFoundException(professionalId));

        var slots = catalogServiceClient.getSlotsForDate(professionalId, date);
        if (slots.isEmpty()) {
            return List.of();
        }

        Set<LocalTime> occupiedStartTimes = catedraAppointmentClient
            .getOccupancies(professionalId, date, date)
            .stream()
            .map(Occupancy::startTime)
            .collect(Collectors.toSet());

        return slots
            .stream()
            .map(slot -> new AvailabilitySlot(slot.startTime(), slot.endTime(), !occupiedStartTimes.contains(slot.startTime())))
            .toList();
    }
}
