package ar.edu.um.prog2.turnosyreservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reservation getReservationSample1() {
        return new Reservation()
            .id(1L)
            .externalReservationId(1L)
            .professionalId(1L)
            .professionalFirstName("professionalFirstName1")
            .professionalLastName("professionalLastName1")
            .startTime("startTime1")
            .endTime("endTime1")
            .patientFirstName("patientFirstName1")
            .patientLastName("patientLastName1")
            .patientPhone("patientPhone1")
            .cancellationReason("cancellationReason1");
    }

    public static Reservation getReservationSample2() {
        return new Reservation()
            .id(2L)
            .externalReservationId(2L)
            .professionalId(2L)
            .professionalFirstName("professionalFirstName2")
            .professionalLastName("professionalLastName2")
            .startTime("startTime2")
            .endTime("endTime2")
            .patientFirstName("patientFirstName2")
            .patientLastName("patientLastName2")
            .patientPhone("patientPhone2")
            .cancellationReason("cancellationReason2");
    }

    public static Reservation getReservationRandomSampleGenerator() {
        return new Reservation()
            .id(longCount.incrementAndGet())
            .externalReservationId(longCount.incrementAndGet())
            .professionalId(longCount.incrementAndGet())
            .professionalFirstName(UUID.randomUUID().toString())
            .professionalLastName(UUID.randomUUID().toString())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString())
            .patientFirstName(UUID.randomUUID().toString())
            .patientLastName(UUID.randomUUID().toString())
            .patientPhone(UUID.randomUUID().toString())
            .cancellationReason(UUID.randomUUID().toString());
    }
}
