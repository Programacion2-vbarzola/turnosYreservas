package ar.edu.um.prog2.turnosyreservas.reservation.domain.model;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ReservationStatus;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Pure domain model for a Reservation. No persistence annotations — see
 * {@code reservation.infrastructure.persistence.entity.ReservationEntity} for the JPA mapping.
 */
public class Reservation {

    private Long id;
    private Long externalReservationId;
    private Long professionalId;
    private String professionalFirstName;
    private String professionalLastName;
    private LocalDate appointmentDate;
    private String startTime;
    private String endTime;
    private String patientFirstName;
    private String patientLastName;
    private String patientPhone;
    private ReservationStatus status;
    private Instant createdAt;
    private Instant confirmedAt;
    private Instant cancelledAt;
    private String cancellationReason;
    private Long processId;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalReservationId() {
        return externalReservationId;
    }

    public void setExternalReservationId(Long externalReservationId) {
        this.externalReservationId = externalReservationId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getProfessionalFirstName() {
        return professionalFirstName;
    }

    public void setProfessionalFirstName(String professionalFirstName) {
        this.professionalFirstName = professionalFirstName;
    }

    public String getProfessionalLastName() {
        return professionalLastName;
    }

    public void setProfessionalLastName(String professionalLastName) {
        this.professionalLastName = professionalLastName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Instant confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
