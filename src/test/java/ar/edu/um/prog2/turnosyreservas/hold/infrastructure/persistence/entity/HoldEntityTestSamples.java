package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HoldEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HoldEntity getHoldEntitySample1() {
        HoldEntity hold = new HoldEntity();
        hold.setId(1L);
        hold.setExternalHoldId("externalHoldId1");
        hold.setExternalProcessId("externalProcessId1");
        hold.setProfessionalId(1L);
        hold.setStartTime("startTime1");
        hold.setEndTime("endTime1");
        return hold;
    }

    public static HoldEntity getHoldEntitySample2() {
        HoldEntity hold = new HoldEntity();
        hold.setId(2L);
        hold.setExternalHoldId("externalHoldId2");
        hold.setExternalProcessId("externalProcessId2");
        hold.setProfessionalId(2L);
        hold.setStartTime("startTime2");
        hold.setEndTime("endTime2");
        return hold;
    }

    public static HoldEntity getHoldEntityRandomSampleGenerator() {
        HoldEntity hold = new HoldEntity();
        hold.setId(longCount.incrementAndGet());
        hold.setExternalHoldId(UUID.randomUUID().toString());
        hold.setExternalProcessId(UUID.randomUUID().toString());
        hold.setProfessionalId(longCount.incrementAndGet());
        hold.setStartTime(UUID.randomUUID().toString());
        hold.setEndTime(UUID.randomUUID().toString());
        return hold;
    }
}
