package ar.edu.um.prog2.turnosyreservas.broker;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements Supplier<String> {

    @Override
    public String get() {
        return "kafka_producer";
    }
}
