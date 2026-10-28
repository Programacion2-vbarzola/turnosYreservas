package ar.edu.um.prog2.turnosyreservas.user.domain.model;

/**
 * Pure domain model for a UserProfile. No persistence annotations — see
 * {@code user.infrastructure.persistence.entity.UserProfileEntity} for the JPA mapping.
 */
public class UserProfile {

    private Long id;
    private String externalPatientId;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalPatientId() {
        return externalPatientId;
    }

    public void setExternalPatientId(String externalPatientId) {
        this.externalPatientId = externalPatientId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
