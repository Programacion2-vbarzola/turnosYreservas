package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JPA entity for ReservationProcess. The pure domain model lives in
 * {@code reservation.domain.model.ReservationProcess}.
 */
@Entity
@Table(name = "reservation_process")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservationProcessEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

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

    @NotNull
    @Size(max = 100)
    @Column(name = "patient_first_name", length = 100, nullable = false)
    private String patientFirstName;

    @NotNull
    @Size(max = 100)
    @Column(name = "patient_last_name", length = 100, nullable = false)
    private String patientLastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProcessStatus status;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Size(max = 200)
    @Column(name = "request_event_id", length = 200)
    private String requestEventId;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "hold_id", nullable = false)
    private Long holdId;

    @ManyToOne(optional = false)
    @NotNull
    private UserEntity user;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPatientFirstName() {
        return this.patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return this.patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public ProcessStatus getStatus() {
        return this.status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRequestEventId() {
        return this.requestEventId;
    }

    public void setRequestEventId(String requestEventId) {
        this.requestEventId = requestEventId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getHoldId() {
        return this.holdId;
    }

    public void setHoldId(Long holdId) {
        this.holdId = holdId;
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
        if (!(o instanceof ReservationProcessEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ReservationProcessEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationProcessEntity{" +
            "id=" + getId() +
            ", externalProcessId='" + getExternalProcessId() + "'" +
            ", professionalId=" + getProfessionalId() +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", patientFirstName='" + getPatientFirstName() + "'" +
            ", patientLastName='" + getPatientLastName() + "'" +
            ", status='" + getStatus() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", requestEventId='" + getRequestEventId() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
