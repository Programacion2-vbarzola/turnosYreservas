package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReservationEntity getReservationEntitySample1() {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(1L);
        reservation.setExternalReservationId(1L);
        reservation.setProfessionalId(1L);
        reservation.setProfessionalFirstName("professionalFirstName1");
        reservation.setProfessionalLastName("professionalLastName1");
        reservation.setStartTime("startTime1");
        reservation.setEndTime("endTime1");
        reservation.setPatientFirstName("patientFirstName1");
        reservation.setPatientLastName("patientLastName1");
        reservation.setPatientPhone("patientPhone1");
        reservation.setCancellationReason("cancellationReason1");
        return reservation;
    }

    public static ReservationEntity getReservationEntitySample2() {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(2L);
        reservation.setExternalReservationId(2L);
        reservation.setProfessionalId(2L);
        reservation.setProfessionalFirstName("professionalFirstName2");
        reservation.setProfessionalLastName("professionalLastName2");
        reservation.setStartTime("startTime2");
        reservation.setEndTime("endTime2");
        reservation.setPatientFirstName("patientFirstName2");
        reservation.setPatientLastName("patientLastName2");
        reservation.setPatientPhone("patientPhone2");
        reservation.setCancellationReason("cancellationReason2");
        return reservation;
    }

    public static ReservationEntity getReservationEntityRandomSampleGenerator() {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(longCount.incrementAndGet());
        reservation.setExternalReservationId(longCount.incrementAndGet());
        reservation.setProfessionalId(longCount.incrementAndGet());
        reservation.setProfessionalFirstName(UUID.randomUUID().toString());
        reservation.setProfessionalLastName(UUID.randomUUID().toString());
        reservation.setStartTime(UUID.randomUUID().toString());
        reservation.setEndTime(UUID.randomUUID().toString());
        reservation.setPatientFirstName(UUID.randomUUID().toString());
        reservation.setPatientLastName(UUID.randomUUID().toString());
        reservation.setPatientPhone(UUID.randomUUID().toString());
        reservation.setCancellationReason(UUID.randomUUID().toString());
        return reservation;
    }
}
