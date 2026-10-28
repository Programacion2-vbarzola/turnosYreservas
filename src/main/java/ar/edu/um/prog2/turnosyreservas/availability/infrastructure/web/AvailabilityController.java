package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.web;

import ar.edu.um.prog2.turnosyreservas.availability.domain.model.AvailabilitySlot;
import ar.edu.um.prog2.turnosyreservas.availability.domain.port.in.GetAvailabilityUseCase;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class AvailabilityController {

    private final GetAvailabilityUseCase getAvailabilityUseCase;

    AvailabilityController(GetAvailabilityUseCase getAvailabilityUseCase) {
        this.getAvailabilityUseCase = getAvailabilityUseCase;
    }

    @GetMapping("/availability")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<AvailabilityResponseDTO> getAvailability(
        @RequestParam Long professionalId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<AvailabilitySlot> slots = getAvailabilityUseCase.getAvailability(professionalId, date);
        List<SlotDTO> slotDTOs = slots.stream().map(s -> new SlotDTO(s.startTime(), s.endTime(), s.available())).toList();
        return ResponseEntity.ok(new AvailabilityResponseDTO(professionalId, date, slotDTOs));
    }

    record AvailabilityResponseDTO(Long professionalId, LocalDate date, List<SlotDTO> slots) {}

    record SlotDTO(LocalTime startTime, LocalTime endTime, boolean available) {}
}
