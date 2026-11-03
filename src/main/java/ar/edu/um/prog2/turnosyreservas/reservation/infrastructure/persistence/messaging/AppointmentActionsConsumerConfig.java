package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.messaging;

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

    public AppointmentActionsConsumerConfig(ProcessPhoneRequestUseCase processPhoneRequestUseCase) {
        this.processPhoneRequestUseCase = processPhoneRequestUseCase;
    }

    @Bean
    public Consumer<AppointmentActionEvent> appointmentActionsConsumer() {
        return event -> {
            LOG.debug("Received appointment action event: type={}, processId={}", event.type(), event.externalProcessId());
            if ("AdditionalInformationRequested".equals(event.type())) {
                processPhoneRequestUseCase.processPhoneRequest(event.externalProcessId());
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AppointmentActionEvent(String type, String externalProcessId) {}
}
