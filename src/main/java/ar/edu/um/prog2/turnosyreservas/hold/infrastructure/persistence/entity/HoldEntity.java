package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JPA entity for Hold. The pure domain model lives in {@code hold.domain.model.Hold}.
 */
@Entity
@Table(name = "hold")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HoldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "external_hold_id", length = 100, nullable = false, unique = true)
    private String externalHoldId;

    @NotNull
    @Size(max = 100)
    @Column(name = "external_process_id", length = 100, nullable = false, unique = true)
    private String externalProcessId;

    @NotNull
    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Size(max = 8)
    @Column(name = "start_time", length = 8, nullable = false)
    private String startTime;

    @Size(max = 8)
    @Column(name = "end_time", length = 8)
    private String endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HoldStatus status;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private UserEntity user;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalHoldId() {
        return this.externalHoldId;
    }

    public void setExternalHoldId(String externalHoldId) {
        this.externalHoldId = externalHoldId;
    }

    public String getExternalProcessId() {
        return this.externalProcessId;
    }

    public void setExternalProcessId(String externalProcessId) {
        this.externalProcessId = externalProcessId;
    }

    public Long getProfessionalId() {
        return this.professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public HoldStatus getStatus() {
        return this.status;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HoldEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((HoldEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoldEntity{" +
            "id=" + getId() +
            ", externalHoldId='" + getExternalHoldId() + "'" +
            ", externalProcessId='" + getExternalProcessId() + "'" +
            ", professionalId=" + getProfessionalId() +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
