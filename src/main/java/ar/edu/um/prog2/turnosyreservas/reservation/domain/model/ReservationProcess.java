package ar.edu.um.prog2.turnosyreservas.reservation.domain.model;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Pure domain model for a ReservationProcess. No persistence annotations — see
 * {@code reservation.infrastructure.persistence.entity.ReservationProcessEntity} for the JPA mapping.
 */
public class ReservationProcess {

    private Long id;
    private String externalProcessId;
    private Long professionalId;
    private LocalDate date;
    private String startTime;
    private String patientFirstName;
    private String patientLastName;
    private ProcessStatus status;
    private Instant expiresAt;
    private String requestEventId;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;
    private Long holdId;
    private Long userId;
    private Long reservationId;

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

    public Long getHoldId() {
        return holdId;
    }

    public void setHoldId(Long holdId) {
        this.holdId = holdId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
