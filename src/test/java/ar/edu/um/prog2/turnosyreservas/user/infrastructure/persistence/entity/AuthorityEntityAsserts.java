package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityEntityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityEntityAllPropertiesEquals(AuthorityEntity expected, AuthorityEntity actual) {
        assertAuthorityEntityUpdatableFieldsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorityEntityUpdatableFieldsEquals(AuthorityEntity expected, AuthorityEntity actual) {
        assertThat(actual)
            .as("Verify AuthorityEntity relevant properties")
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()));
    }
}
