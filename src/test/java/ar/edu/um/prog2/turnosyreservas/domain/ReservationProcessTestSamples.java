package ar.edu.um.prog2.turnosyreservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationProcessTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReservationProcess getReservationProcessSample1() {
        return new ReservationProcess()
            .id(1L)
            .externalProcessId("externalProcessId1")
            .professionalId(1L)
            .startTime("startTime1")
            .patientFirstName("patientFirstName1")
            .patientLastName("patientLastName1")
            .requestEventId("requestEventId1")
            .phoneNumber("phoneNumber1");
    }

    public static ReservationProcess getReservationProcessSample2() {
        return new ReservationProcess()
            .id(2L)
            .externalProcessId("externalProcessId2")
            .professionalId(2L)
            .startTime("startTime2")
            .patientFirstName("patientFirstName2")
            .patientLastName("patientLastName2")
            .requestEventId("requestEventId2")
            .phoneNumber("phoneNumber2");
    }

    public static ReservationProcess getReservationProcessRandomSampleGenerator() {
        return new ReservationProcess()
            .id(longCount.incrementAndGet())
            .externalProcessId(UUID.randomUUID().toString())
            .professionalId(longCount.incrementAndGet())
            .startTime(UUID.randomUUID().toString())
            .patientFirstName(UUID.randomUUID().toString())
            .patientLastName(UUID.randomUUID().toString())
            .requestEventId(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
