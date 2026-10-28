package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentOccupanciesResponseDTO {

    private List<OccupancyItemDTO> occupancies;

    public AppointmentOccupanciesResponseDTO() {}

    public List<OccupancyItemDTO> getOccupancies() {
        return occupancies;
    }

    public void setOccupancies(List<OccupancyItemDTO> occupancies) {
        this.occupancies = occupancies;
    }
}
