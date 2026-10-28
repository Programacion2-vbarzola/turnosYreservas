package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationProcessEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReservationProcessEntity getReservationProcessEntitySample1() {
        ReservationProcessEntity process = new ReservationProcessEntity();
        process.setId(1L);
        process.setExternalProcessId("externalProcessId1");
        process.setProfessionalId(1L);
        process.setStartTime("startTime1");
        process.setPatientFirstName("patientFirstName1");
        process.setPatientLastName("patientLastName1");
        process.setRequestEventId("requestEventId1");
        process.setPhoneNumber("phoneNumber1");
        return process;
    }

    public static ReservationProcessEntity getReservationProcessEntitySample2() {
        ReservationProcessEntity process = new ReservationProcessEntity();
        process.setId(2L);
        process.setExternalProcessId("externalProcessId2");
        process.setProfessionalId(2L);
        process.setStartTime("startTime2");
        process.setPatientFirstName("patientFirstName2");
        process.setPatientLastName("patientLastName2");
        process.setRequestEventId("requestEventId2");
        process.setPhoneNumber("phoneNumber2");
        return process;
    }

    public static ReservationProcessEntity getReservationProcessEntityRandomSampleGenerator() {
        ReservationProcessEntity process = new ReservationProcessEntity();
        process.setId(longCount.incrementAndGet());
        process.setExternalProcessId(UUID.randomUUID().toString());
        process.setProfessionalId(longCount.incrementAndGet());
        process.setStartTime(UUID.randomUUID().toString());
        process.setPatientFirstName(UUID.randomUUID().toString());
        process.setPatientLastName(UUID.randomUUID().toString());
        process.setRequestEventId(UUID.randomUUID().toString());
        process.setPhoneNumber(UUID.randomUUID().toString());
        return process;
    }
}
