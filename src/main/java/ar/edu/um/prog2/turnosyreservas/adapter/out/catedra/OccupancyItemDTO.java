package ar.edu.um.prog2.turnosyreservas.adapter.out.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
class OccupancyItemDTO {

    private LocalDate date;
    private LocalTime startTime;
    private String status;

    OccupancyItemDTO() {}

    LocalDate getDate() {
        return date;
    }

    void setDate(LocalDate date) {
        this.date = date;
    }

    LocalTime getStartTime() {
        return startTime;
    }

    void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }
}
