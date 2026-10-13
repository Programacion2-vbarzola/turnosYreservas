package ar.edu.um.prog2.turnosyreservas.domain.model;

public record ProfessionalInfo(Long id, String firstName, String lastName, Long categoryId, String categoryName, boolean enabled) {}
