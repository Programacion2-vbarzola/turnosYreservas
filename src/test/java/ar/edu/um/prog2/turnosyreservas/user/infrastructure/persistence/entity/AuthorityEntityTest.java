package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import static ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.AuthorityEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorityEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorityEntity.class);
        AuthorityEntity authority1 = getAuthorityEntitySample1();
        AuthorityEntity authority2 = new AuthorityEntity();
        assertThat(authority1).isNotEqualTo(authority2);

        authority2.setName(authority1.getName());
        assertThat(authority1).isEqualTo(authority2);

        authority2 = getAuthorityEntitySample2();
        assertThat(authority1).isNotEqualTo(authority2);
    }

    @Test
    void hashCodeVerifier() {
        AuthorityEntity authority = new AuthorityEntity();
        assertThat(authority.hashCode()).isZero();

        AuthorityEntity authority1 = getAuthorityEntitySample1();
        authority.setName(authority1.getName());
        assertThat(authority).hasSameHashCodeAs(authority1);
    }
}
