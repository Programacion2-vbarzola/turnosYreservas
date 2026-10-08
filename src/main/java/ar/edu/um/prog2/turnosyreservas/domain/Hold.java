package ar.edu.um.prog2.turnosyreservas.domain;

import ar.edu.um.prog2.turnosyreservas.domain.enumeration.HoldStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Hold.
 */
@Entity
@Table(name = "hold")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hold implements Serializable {

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
    private User user;

    @JsonIgnoreProperties(value = { "hold", "user", "reservation" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "hold")
    private ReservationProcess reservationProcess;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hold id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalHoldId() {
        return this.externalHoldId;
    }

    public Hold externalHoldId(String externalHoldId) {
        this.setExternalHoldId(externalHoldId);
        return this;
    }

    public void setExternalHoldId(String externalHoldId) {
        this.externalHoldId = externalHoldId;
    }

    public String getExternalProcessId() {
        return this.externalProcessId;
    }

    public Hold externalProcessId(String externalProcessId) {
        this.setExternalProcessId(externalProcessId);
        return this;
    }

    public void setExternalProcessId(String externalProcessId) {
        this.externalProcessId = externalProcessId;
    }

    public Long getProfessionalId() {
        return this.professionalId;
    }

    public Hold professionalId(Long professionalId) {
        this.setProfessionalId(professionalId);
        return this;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Hold date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Hold startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public Hold endTime(String endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public HoldStatus getStatus() {
        return this.status;
    }

    public Hold status(HoldStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public Hold expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Hold createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hold user(User user) {
        this.setUser(user);
        return this;
    }

    public ReservationProcess getReservationProcess() {
        return this.reservationProcess;
    }

    public void setReservationProcess(ReservationProcess reservationProcess) {
        if (this.reservationProcess != null) {
            this.reservationProcess.setHold(null);
        }
        if (reservationProcess != null) {
            reservationProcess.setHold(this);
        }
        this.reservationProcess = reservationProcess;
    }

    public Hold reservationProcess(ReservationProcess reservationProcess) {
        this.setReservationProcess(reservationProcess);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hold)) {
            return false;
        }
        return getId() != null && getId().equals(((Hold) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hold{" +
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
