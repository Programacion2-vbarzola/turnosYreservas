package ar.edu.um.prog2.turnosyreservas.service.dto;

import ar.edu.um.prog2.turnosyreservas.domain.enumeration.HoldStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.prog2.turnosyreservas.domain.Hold} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HoldDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String externalHoldId;

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

    @Size(max = 8)
    private String endTime;

    @NotNull
    private HoldStatus status;

    @NotNull
    private Instant expiresAt;

    @NotNull
    private Instant createdAt;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalHoldId() {
        return externalHoldId;
    }

    public void setExternalHoldId(String externalHoldId) {
        this.externalHoldId = externalHoldId;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public HoldStatus getStatus() {
        return status;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
        if (!(o instanceof HoldDTO)) {
            return false;
        }

        HoldDTO holdDTO = (HoldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, holdDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoldDTO{" +
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
            ", user=" + getUser() +
            "}";
    }
}
