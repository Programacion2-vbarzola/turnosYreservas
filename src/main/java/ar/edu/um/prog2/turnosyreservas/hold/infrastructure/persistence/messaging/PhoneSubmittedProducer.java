package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.messaging;

import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.AppointmentKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class PhoneSubmittedProducer implements AppointmentKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneSubmittedProducer.class);
    private static final String BINDING = "phoneSubmittedProducer-out-0";

    private final StreamBridge streamBridge;

    public PhoneSubmittedProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendPhone(String externalProcessId, String phoneNumber) {
        PhoneSubmittedEvent event = new PhoneSubmittedEvent(externalProcessId, phoneNumber);
        LOG.debug("Sending phone submitted event: processId={}", externalProcessId);
        streamBridge.send(BINDING, event);
    }

    public record PhoneSubmittedEvent(String externalProcessId, String phoneNumber) {}
}
