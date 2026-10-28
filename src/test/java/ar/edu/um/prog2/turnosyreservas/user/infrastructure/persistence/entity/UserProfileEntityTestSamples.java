package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfileEntity getUserProfileEntitySample1() {
        UserProfileEntity profile = new UserProfileEntity();
        profile.setId(1L);
        profile.setExternalPatientId("externalPatientId1");
        return profile;
    }

    public static UserProfileEntity getUserProfileEntitySample2() {
        UserProfileEntity profile = new UserProfileEntity();
        profile.setId(2L);
        profile.setExternalPatientId("externalPatientId2");
        return profile;
    }

    public static UserProfileEntity getUserProfileEntityRandomSampleGenerator() {
        UserProfileEntity profile = new UserProfileEntity();
        profile.setId(longCount.incrementAndGet());
        profile.setExternalPatientId(UUID.randomUUID().toString());
        return profile;
    }
}
