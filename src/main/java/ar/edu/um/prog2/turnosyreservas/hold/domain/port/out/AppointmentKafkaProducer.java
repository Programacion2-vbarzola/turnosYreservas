package ar.edu.um.prog2.turnosyreservas.hold.domain.port.out;

public interface AppointmentKafkaProducer {
    void sendPhone(String externalProcessId, String phoneNumber);
}
