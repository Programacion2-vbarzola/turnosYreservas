package ar.edu.um.prog2.turnosyreservas.service.dto;

import ar.edu.um.prog2.turnosyreservas.domain.enumeration.ProcessStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservationProcessDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String externalProcessId;

    @NotNull
    private Long professionalId;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(max = 8)
    private String startTime;

    @NotNull
    @Size(max = 100)
    private String patientFirstName;

    @NotNull
    @Size(max = 100)
    private String patientLastName;

    @NotNull
    private ProcessStatus status;

    private Instant expiresAt;

    @Size(max = 200)
    private String requestEventId;

    @Size(max = 20)
    private String phoneNumber;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private HoldDTO hold;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalProcessId() {
        return externalProcessId;
    }

    public void setExternalProcessId(String externalProcessId) {
        this.externalProcessId = externalProcessId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRequestEventId() {
        return requestEventId;
    }

    public void setRequestEventId(String requestEventId) {
        this.requestEventId = requestEventId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public HoldDTO getHold() {
        return hold;
    }

    public void setHold(HoldDTO hold) {
        this.hold = hold;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationProcessDTO)) {
            return false;
        }

        ReservationProcessDTO reservationProcessDTO = (ReservationProcessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservationProcessDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationProcessDTO{" +
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
            ", hold=" + getHold() +
            ", user=" + getUser() +
            "}";
    }
}
