package ar.edu.um.prog2.turnosyreservas.adapter.out.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class AppointmentOccupanciesResponseDTO {

    private List<OccupancyItemDTO> occupancies;

    AppointmentOccupanciesResponseDTO() {}

    List<OccupancyItemDTO> getOccupancies() {
        return occupancies;
    }

    void setOccupancies(List<OccupancyItemDTO> occupancies) {
        this.occupancies = occupancies;
    }
}
