package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.web;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.in.GetProcessStatusUseCase;
import ar.edu.um.prog2.turnosyreservas.security.SecurityUtils;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ReservationController {

    private final GetProcessStatusUseCase getProcessStatusUseCase;
    private final UserRepositoryPort userRepositoryPort;

    ReservationController(GetProcessStatusUseCase getProcessStatusUseCase, UserRepositoryPort userRepositoryPort) {
        this.getProcessStatusUseCase = getProcessStatusUseCase;
        this.userRepositoryPort = userRepositoryPort;
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
}
