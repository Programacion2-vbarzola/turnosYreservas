package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.messaging;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleAppointmentConfirmedUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.HandleFinalStateUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.ProcessPhoneRequestUseCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public AppointmentActionsConsumerConfig(
        ProcessPhoneRequestUseCase processPhoneRequestUseCase,
        HandleAppointmentConfirmedUseCase handleAppointmentConfirmedUseCase,
        HandleFinalStateUseCase handleFinalStateUseCase
    ) {
        this.processPhoneRequestUseCase = processPhoneRequestUseCase;
        this.handleAppointmentConfirmedUseCase = handleAppointmentConfirmedUseCase;
        this.handleFinalStateUseCase = handleFinalStateUseCase;
    }

    @Bean
    public Consumer<AppointmentActionEvent> appointmentActionsConsumer() {
        return event -> {
            LOG.debug("Received appointment action event: type={}, processId={}", event.type(), event.externalProcessId());
            switch (event.type()) {
                case "AdditionalInformationRequested" -> processPhoneRequestUseCase.processPhoneRequest(event.externalProcessId());
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

    //Usamos para externalReservationID un objeto Long porque es nullable y el record se usa para todos los tipos de eventos.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AppointmentActionEvent(String type, String externalProcessId, Long externalReservationId) {}
}
