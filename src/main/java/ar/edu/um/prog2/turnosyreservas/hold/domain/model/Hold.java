package ar.edu.um.prog2.turnosyreservas.hold.domain.model;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.enumeration.HoldStatus;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Pure domain model for a Hold. No persistence annotations — see
 * {@code hold.infrastructure.persistence.entity.HoldEntity} for the JPA mapping.
 */
public class Hold {

    private Long id;
    private String externalHoldId;
    private String externalProcessId;
    private Long professionalId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private HoldStatus status;
    private Instant expiresAt;
    private Instant createdAt;
    private Long userId;
    private Long reservationProcessId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReservationProcessId() {
        return reservationProcessId;
    }

    public void setReservationProcessId(Long reservationProcessId) {
        this.reservationProcessId = reservationProcessId;
    }
}
