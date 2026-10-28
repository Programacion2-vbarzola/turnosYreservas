package ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra;

import ar.edu.um.prog2.turnosyreservas.availability.domain.model.Occupancy;
import ar.edu.um.prog2.turnosyreservas.availability.domain.port.out.CatedraAppointmentClient;
import ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto.AppointmentOccupanciesResponseDTO;
import ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto.HoldRequestDTO;
import ar.edu.um.prog2.turnosyreservas.availability.infrastructure.external.catedra.dto.HoldResponseDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CatedraRestAdapter implements CatedraAppointmentClient {

    private static final Logger LOG = LoggerFactory.getLogger(CatedraRestAdapter.class);

    private final RestClient restClient;
    private final String catedraJwt;

    public CatedraRestAdapter(
        @Value("${catedra.api.base-url:http://localhost:9090}") String baseUrl,
        @Value("${catedra.api.jwt:changeme}") String catedraJwt
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.catedraJwt = catedraJwt;
    }

    @Override
    public List<Occupancy> getOccupancies(Long professionalId, LocalDate from, LocalDate to) {
        try {
            AppointmentOccupanciesResponseDTO response = restClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder
                        .path("/api/appointment-occupancies")
                        .queryParam("professionalId", professionalId)
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build()
                )
                .header("Authorization", "Bearer " + catedraJwt)
                .retrieve()
                .body(AppointmentOccupanciesResponseDTO.class);

            if (response == null || response.getOccupancies() == null) {
                return List.of();
            }

            return response
                .getOccupancies()
                .stream()
                .filter(item -> item.getDate() != null && item.getStartTime() != null && item.getStatus() != null)
                .map(item -> new Occupancy(item.getDate(), item.getStartTime(), item.getStatus()))
                .toList();
        } catch (HttpClientErrorException.NotFound e) {
            LOG.warn("No occupancies found for professional {} from {} to {}", professionalId, from, to);
            return List.of();
        } catch (Exception e) {
            LOG.error("Error fetching occupancies for professional {} from {} to {}: {}", professionalId, from, to, e.getMessage());
            return List.of();
        }
    }

    @Override
    public HoldResponse createHold(Long professionalId, LocalDate date, LocalTime startTime, String externalPatientId) {
        HoldRequestDTO request = new HoldRequestDTO(professionalId, date, startTime, externalPatientId);
        HoldResponseDTO response = restClient
            .post()
            .uri("/api/appointment-holds")
            .header("Authorization", "Bearer " + catedraJwt)
            .body(request)
            .retrieve()
            .body(HoldResponseDTO.class);
        if (response == null) {
            throw new IllegalStateException("Empty response from catedra service when creating hold");
        }
        return new HoldResponse(response.externalHoldId(), response.externalProcessId(), response.expiresAt());
    }
}
