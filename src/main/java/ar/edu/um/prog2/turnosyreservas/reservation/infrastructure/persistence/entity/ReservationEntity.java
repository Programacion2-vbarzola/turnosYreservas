package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ReservationStatus;
import ar.edu.um.prog2.turnosyreservas.user.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JPA entity for Reservation. The pure domain model lives in
 * {@code reservation.domain.model.Reservation}.
 */
@Entity
@Table(name = "reservation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "external_reservation_id")
    private Long externalReservationId;

    @NotNull
    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Size(max = 100)
    @Column(name = "professional_first_name", length = 100)
    private String professionalFirstName;

    @Size(max = 100)
    @Column(name = "professional_last_name", length = 100)
    private String professionalLastName;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @NotNull
    @Size(max = 8)
    @Column(name = "start_time", length = 8, nullable = false)
    private String startTime;

    @Size(max = 8)
    @Column(name = "end_time", length = 8)
    private String endTime;

    @NotNull
    @Size(max = 100)
    @Column(name = "patient_first_name", length = 100, nullable = false)
    private String patientFirstName;

    @NotNull
    @Size(max = 100)
    @Column(name = "patient_last_name", length = 100, nullable = false)
    private String patientLastName;

    @Size(max = 20)
    @Column(name = "patient_phone", length = 20)
    private String patientPhone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Size(max = 500)
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ReservationProcessEntity process;

    @ManyToOne(optional = false)
    @NotNull
    private UserEntity user;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalReservationId() {
        return this.externalReservationId;
    }

    public void setExternalReservationId(Long externalReservationId) {
        this.externalReservationId = externalReservationId;
    }

    public Long getProfessionalId() {
        return this.professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getProfessionalFirstName() {
        return this.professionalFirstName;
    }

    public void setProfessionalFirstName(String professionalFirstName) {
        this.professionalFirstName = professionalFirstName;
    }

    public String getProfessionalLastName() {
        return this.professionalLastName;
    }

    public void setProfessionalLastName(String professionalLastName) {
        this.professionalLastName = professionalLastName;
    }

    public LocalDate getAppointmentDate() {
        return this.appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public String getPatientPhone() {
        return this.patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public ReservationStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getConfirmedAt() {
        return this.confirmedAt;
    }

    public void setConfirmedAt(Instant confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Instant getCancelledAt() {
        return this.cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public ReservationProcessEntity getProcess() {
        return this.process;
    }

    public void setProcess(ReservationProcessEntity process) {
        this.process = process;
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
        if (!(o instanceof ReservationEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ReservationEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationEntity{" +
            "id=" + getId() +
            ", externalReservationId=" + getExternalReservationId() +
            ", professionalId=" + getProfessionalId() +
            ", professionalFirstName='" + getProfessionalFirstName() + "'" +
            ", professionalLastName='" + getProfessionalLastName() + "'" +
            ", appointmentDate='" + getAppointmentDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", patientFirstName='" + getPatientFirstName() + "'" +
            ", patientLastName='" + getPatientLastName() + "'" +
            ", patientPhone='" + getPatientPhone() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", confirmedAt='" + getConfirmedAt() + "'" +
            ", cancelledAt='" + getCancelledAt() + "'" +
            ", cancellationReason='" + getCancellationReason() + "'" +
            "}";
    }
}
