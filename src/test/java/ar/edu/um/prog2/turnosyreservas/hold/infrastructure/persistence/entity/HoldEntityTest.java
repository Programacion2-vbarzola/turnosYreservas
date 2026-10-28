package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity;

import static ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity.HoldEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoldEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoldEntity.class);
        HoldEntity hold1 = getHoldEntitySample1();
        HoldEntity hold2 = new HoldEntity();
        assertThat(hold1).isNotEqualTo(hold2);

        hold2.setId(hold1.getId());
        assertThat(hold1).isEqualTo(hold2);

        hold2 = getHoldEntitySample2();
        assertThat(hold1).isNotEqualTo(hold2);
    }
}
