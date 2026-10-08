package ar.edu.um.prog2.turnosyreservas.domain;

import ar.edu.um.prog2.turnosyreservas.domain.enumeration.ProcessStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A ReservationProcess.
 */
@Entity
@Table(name = "reservation_process")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservationProcess implements Serializable {

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

    @JsonIgnoreProperties(value = { "user", "reservationProcess" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Hold hold;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @JsonIgnoreProperties(value = { "process", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "process")
    private Reservation reservation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReservationProcess id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalProcessId() {
        return this.externalProcessId;
    }

    public ReservationProcess externalProcessId(String externalProcessId) {
        this.setExternalProcessId(externalProcessId);
        return this;
    }

    public void setExternalProcessId(String externalProcessId) {
        this.externalProcessId = externalProcessId;
    }

    public Long getProfessionalId() {
        return this.professionalId;
    }

    public ReservationProcess professionalId(Long professionalId) {
        this.setProfessionalId(professionalId);
        return this;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ReservationProcess date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public ReservationProcess startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPatientFirstName() {
        return this.patientFirstName;
    }

    public ReservationProcess patientFirstName(String patientFirstName) {
        this.setPatientFirstName(patientFirstName);
        return this;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return this.patientLastName;
    }

    public ReservationProcess patientLastName(String patientLastName) {
        this.setPatientLastName(patientLastName);
        return this;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public ProcessStatus getStatus() {
        return this.status;
    }

    public ReservationProcess status(ProcessStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public ReservationProcess expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRequestEventId() {
        return this.requestEventId;
    }

    public ReservationProcess requestEventId(String requestEventId) {
        this.setRequestEventId(requestEventId);
        return this;
    }

    public void setRequestEventId(String requestEventId) {
        this.requestEventId = requestEventId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public ReservationProcess phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ReservationProcess createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ReservationProcess updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Hold getHold() {
        return this.hold;
    }

    public void setHold(Hold hold) {
        this.hold = hold;
    }

    public ReservationProcess hold(Hold hold) {
        this.setHold(hold);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReservationProcess user(User user) {
        this.setUser(user);
        return this;
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public void setReservation(Reservation reservation) {
        if (this.reservation != null) {
            this.reservation.setProcess(null);
        }
        if (reservation != null) {
            reservation.setProcess(this);
        }
        this.reservation = reservation;
    }

    public ReservationProcess reservation(Reservation reservation) {
        this.setReservation(reservation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationProcess)) {
            return false;
        }
        return getId() != null && getId().equals(((ReservationProcess) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationProcess{" +
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
