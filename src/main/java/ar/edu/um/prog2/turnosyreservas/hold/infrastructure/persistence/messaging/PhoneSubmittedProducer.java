package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.messaging;

import ar.edu.um.prog2.turnosyreservas.hold.domain.port.out.AppointmentKafkaProducer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class PhoneSubmittedProducer implements AppointmentKafkaProducer {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneSubmittedProducer.class);
    private static final String BINDING = "phoneSubmittedProducer-out-0";

    private final StreamBridge streamBridge;
    private final String groupId;

    public PhoneSubmittedProducer(StreamBridge streamBridge, @Value("${catedra.api.group-id}") String groupId) {
        this.streamBridge = streamBridge;
        this.groupId = groupId;
    }

    @Override
    public void sendPhone(String externalProcessId, String externalHoldId, String requestEventId, String phoneNumber) {
        PhoneSubmittedEvent event = new PhoneSubmittedEvent(
            UUID.randomUUID().toString(),
            "AdditionalInformationSubmitted",
            groupId,
            externalProcessId,
            externalHoldId,
            requestEventId,
            new PhoneInformation(phoneNumber),
            Instant.now().toString(),
            1
        );
        LOG.debug("Sending phone submitted event: processId={}, holdId={}", externalProcessId, externalHoldId);
        Message<PhoneSubmittedEvent> message = MessageBuilder.withPayload(event)
            .setHeader(KafkaHeaders.KEY, externalProcessId.getBytes(StandardCharsets.UTF_8))
            .build();
        streamBridge.send(BINDING, message);
    }

    public record PhoneInformation(String phoneNumber) {}

    public record PhoneSubmittedEvent(
        String eventId,
        String eventType,
        String groupId,
        String reservationProcessId,
        String holdId,
        String requestEventId,
        PhoneInformation information,
        String occurredAt,
        int schemaVersion
    ) {}
}
