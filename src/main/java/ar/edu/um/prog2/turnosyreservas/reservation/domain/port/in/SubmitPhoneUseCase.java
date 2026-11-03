package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in;

import ar.edu.um.prog2.turnosyreservas.user.domain.model.User;

public interface SubmitPhoneUseCase {
    record PhoneSubmittedResult(Long reservationProcessId, String status) {}
    PhoneSubmittedResult submitPhone(Long processId, String phoneNumber, User user);
}
