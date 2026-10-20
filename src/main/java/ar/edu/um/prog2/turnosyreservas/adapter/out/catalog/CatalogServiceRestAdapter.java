package ar.edu.um.prog2.turnosyreservas.adapter.out.catalog;

import static ar.edu.um.prog2.turnosyreservas.security.SecurityUtils.AUTHORITIES_CLAIM;
import static ar.edu.um.prog2.turnosyreservas.security.SecurityUtils.JWT_ALGORITHM;

import ar.edu.um.prog2.turnosyreservas.domain.model.ProfessionalInfo;
import ar.edu.um.prog2.turnosyreservas.domain.model.ScheduleSlot;
import ar.edu.um.prog2.turnosyreservas.domain.port.out.CatalogServiceClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CatalogServiceRestAdapter implements CatalogServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogServiceRestAdapter.class);

    private final RestClient restClient;
    private final JwtEncoder jwtEncoder;

    public CatalogServiceRestAdapter(
        @Value("${application.catalog-service.base-url:http://localhost:8081}") String baseUrl,
        JwtEncoder jwtEncoder
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.jwtEncoder = jwtEncoder;
    }

    private String mintServiceToken() {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(now.plus(5, ChronoUnit.MINUTES))
            .subject("turnosyreservas-service")
            .claim(AUTHORITIES_CLAIM, "ROLE_USER")
            .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(JWT_ALGORITHM).build(), claims)).getTokenValue();
    }

    @Override
    public Optional<ProfessionalInfo> getProfessional(Long professionalId) {
        try {
            ProfessionalResponseDTO dto = restClient
                .get()
                .uri("/api/professionals/{id}", professionalId)
                .header("Authorization", "Bearer " + mintServiceToken())
                .retrieve()
                .body(ProfessionalResponseDTO.class);
            if (dto == null) return Optional.empty();
            return Optional.of(
                new ProfessionalInfo(
                    dto.getId(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getCategoryId(),
                    dto.getCategoryName(),
                    dto.isEnabled()
                )
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            LOG.error("Error fetching professional {} from catalog service: {}", professionalId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<ScheduleSlot> getSlotsForDate(Long professionalId, LocalDate date) {
        // TODO Sprint 2: call Repo 2 catalog snapshot and compute slots for the given date
        LOG.warn("getSlotsForDate not yet implemented — returning empty list for professional {} on {}", professionalId, date);
        return List.of();
    }
}
