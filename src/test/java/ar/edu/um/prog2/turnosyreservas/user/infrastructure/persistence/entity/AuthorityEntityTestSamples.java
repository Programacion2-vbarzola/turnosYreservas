package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import java.util.UUID;

public class AuthorityEntityTestSamples {

    public static AuthorityEntity getAuthorityEntitySample1() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setName("name1");
        return authority;
    }

    public static AuthorityEntity getAuthorityEntitySample2() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setName("name2");
        return authority;
    }

    public static AuthorityEntity getAuthorityEntityRandomSampleGenerator() {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setName(UUID.randomUUID().toString());
        return authority;
    }
}
