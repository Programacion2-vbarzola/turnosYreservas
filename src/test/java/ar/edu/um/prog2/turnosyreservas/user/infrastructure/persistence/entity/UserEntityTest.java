package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    private static final Long DEFAULT_ID = 1L;

    @Test
    void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(UserEntity.class);
        UserEntity user1 = new UserEntity();
        user1.setId(DEFAULT_ID);
        UserEntity user2 = new UserEntity();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }
}
