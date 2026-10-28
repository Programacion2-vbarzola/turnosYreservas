package ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.domain.Persistable;

/**
 * JPA entity for Authority. Managed directly by {@code AuthorityResource} and
 * {@code user.infrastructure.persistence.repository.AuthorityJpaRepository} — no pure domain
 * counterpart, since {@code user.domain.model.User.authorities} is already a {@code Set<String>}.
 */
@Entity
@Table(name = "jhi_authority")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityEntity implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Override
    public String getId() {
        return this.name;
    }

    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AuthorityEntity setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityEntity)) {
            return false;
        }
        return getName() != null && getName().equals(((AuthorityEntity) o).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityEntity{" +
            "name=" + getName() +
            "}";
    }
}
