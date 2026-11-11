package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.web;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.CancelReservationUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.GetProcessStatusUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.GetUserReservationsUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.SubmitPhoneUseCase;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.web.dto.SubmitPhoneRequest;
import ar.edu.um.prog2.turnosyreservas.security.SecurityUtils;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserRepositoryPort;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ReservationController {

    private final GetProcessStatusUseCase getProcessStatusUseCase;
    private final SubmitPhoneUseCase submitPhoneUseCase;
    private final UserRepositoryPort userRepositoryPort;
    private final GetUserReservationsUseCase getUserReservationsUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;

    ReservationController(
        GetProcessStatusUseCase getProcessStatusUseCase,
        SubmitPhoneUseCase submitPhoneUseCase,
        UserRepositoryPort userRepositoryPort,
        GetUserReservationsUseCase getUserReservationsUseCase,
        CancelReservationUseCase cancelReservationUseCase
    ) {
        this.getProcessStatusUseCase = getProcessStatusUseCase;
        this.submitPhoneUseCase = submitPhoneUseCase;
        this.userRepositoryPort = userRepositoryPort;
        this.getUserReservationsUseCase = getUserReservationsUseCase;
        this.cancelReservationUseCase = cancelReservationUseCase;
    }

    @GetMapping("/processes/{processId}/status")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<GetProcessStatusUseCase.ProcessStatusResult> getProcessStatus(@PathVariable Long processId) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("No authenticated user"));

        var user = userRepositoryPort
            .findOneWithAuthoritiesByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));

        return ResponseEntity.ok(getProcessStatusUseCase.getProcessStatus(processId, user));
    }

    @PostMapping("/processes/{processId}/phone")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<SubmitPhoneUseCase.PhoneSubmittedResult> submitPhone(
        @PathVariable Long processId,
        @RequestBody @Valid SubmitPhoneRequest request
    ) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("No authenticated user"));

        var user = userRepositoryPort
            .findOneWithAuthoritiesByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));

        return ResponseEntity.status(202).body(submitPhoneUseCase.submitPhone(processId, request.phoneNumber(), user));
    }

    //Get all de reservations en forma de lista por un usuario
    @GetMapping("/appointments")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<List<Reservation>> getUserReservations() {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("No authenticated user"));

        var user = userRepositoryPort
            .findOneWithAuthoritiesByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));

        return ResponseEntity.ok(getUserReservationsUseCase.getUserReservations(user.getId()));
    }

    @PostMapping("/appointments/{reservationProcessId}/cancel")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CancelReservationUseCase.CancelReservationResult> cancelReservation(
        @PathVariable Long reservationProcessId,
        @RequestBody CancelRequest request
    ) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("No authenticated user"));

        var user = userRepositoryPort
            .findOneWithAuthoritiesByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));

        return ResponseEntity.ok(cancelReservationUseCase.cancelReservation(reservationProcessId, request.reason(), user));
    }

    record CancelRequest(String reason) {}
}
