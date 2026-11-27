package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.messaging;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleAppointmentConfirmedUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleFinalStateUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.ProcessPhoneRequestUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ProcessedEventRepositoryPort;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentActionsConsumerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentActionsConsumerConfig.class);

    private final ProcessPhoneRequestUseCase processPhoneRequestUseCase;
    private final HandleAppointmentConfirmedUseCase handleAppointmentConfirmedUseCase;
    private final HandleFinalStateUseCase handleFinalStateUseCase;
    private final ProcessedEventRepositoryPort processedEventRepositoryPort;

    public AppointmentActionsConsumerConfig(
        ProcessPhoneRequestUseCase processPhoneRequestUseCase,
        HandleAppointmentConfirmedUseCase handleAppointmentConfirmedUseCase,
        HandleFinalStateUseCase handleFinalStateUseCase,
        ProcessedEventRepositoryPort processedEventRepositoryPort
    ) {
        this.processPhoneRequestUseCase = processPhoneRequestUseCase;
        this.handleAppointmentConfirmedUseCase = handleAppointmentConfirmedUseCase;
        this.handleFinalStateUseCase = handleFinalStateUseCase;
        this.processedEventRepositoryPort = processedEventRepositoryPort;
    }

    @Bean
    public Consumer<AppointmentActionEvent> appointmentActionsConsumer() {
        return event -> {
            LOG.debug("Received appointment action event: type={}, processId={}", event.type(), event.externalProcessId());

            if (processedEventRepositoryPort.existsByEventId(event.eventId())) {
                LOG.warn("Duplicate event ignored: eventId={}", event.eventId());
                return;
            }
            processedEventRepositoryPort.save(event.eventId());

            switch (event.type()) {
                case "AdditionalInformationRequested" -> processPhoneRequestUseCase.processPhoneRequest(
                    event.externalProcessId(),
                    event.eventId()
                );
                case "AppointmentConfirmed" -> handleAppointmentConfirmedUseCase.handleConfirmed(
                    event.externalProcessId(),
                    event.externalReservationId()
                );
                case "AdditionalInformationRejected",
                    "AppointmentProcessExpired",
                    "AppointmentProcessInvalid" -> handleFinalStateUseCase.handleFinalState(event.externalProcessId(), event.type());
                default -> LOG.warn("Unknown appointment action event type: {}", event.type());
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AppointmentActionEvent(
        @JsonProperty("eventId") String eventId,
        @JsonProperty("eventType") String type,
        @JsonProperty("reservationProcessId") String externalProcessId,
        @JsonProperty("reservationId") Long externalReservationId
    ) {}
}
