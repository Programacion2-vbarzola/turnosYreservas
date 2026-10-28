package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import static ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserProfileEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProfileEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfileEntity.class);
        UserProfileEntity userProfile1 = getUserProfileEntitySample1();
        UserProfileEntity userProfile2 = new UserProfileEntity();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileEntitySample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }
}
