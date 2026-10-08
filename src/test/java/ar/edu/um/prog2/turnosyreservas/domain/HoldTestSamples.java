package ar.edu.um.prog2.turnosyreservas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HoldTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hold getHoldSample1() {
        return new Hold()
            .id(1L)
            .externalHoldId("externalHoldId1")
            .externalProcessId("externalProcessId1")
            .professionalId(1L)
            .startTime("startTime1")
            .endTime("endTime1");
    }

    public static Hold getHoldSample2() {
        return new Hold()
            .id(2L)
            .externalHoldId("externalHoldId2")
            .externalProcessId("externalProcessId2")
            .professionalId(2L)
            .startTime("startTime2")
            .endTime("endTime2");
    }

    public static Hold getHoldRandomSampleGenerator() {
        return new Hold()
            .id(longCount.incrementAndGet())
            .externalHoldId(UUID.randomUUID().toString())
            .externalProcessId(UUID.randomUUID().toString())
            .professionalId(longCount.incrementAndGet())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString());
    }
}
