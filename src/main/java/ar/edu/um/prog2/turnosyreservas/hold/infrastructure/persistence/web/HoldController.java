package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.web;

import ar.edu.um.prog2.turnosyreservas.hold.domain.port.in.ConfirmHoldUseCase;
import ar.edu.um.prog2.turnosyreservas.hold.domain.port.in.CreateHoldUseCase;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.web.dto.ConfirmHoldRequest;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.web.dto.HoldRequest;
import ar.edu.um.prog2.turnosyreservas.security.SecurityUtils;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserProfileRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.user.domain.port.out.UserRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class HoldController {

    private final CreateHoldUseCase createHoldUseCase;
    private final UserRepositoryPort userRepositoryPort;
    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final ConfirmHoldUseCase confirmHoldUseCase;

    HoldController(
        CreateHoldUseCase createHoldUseCase,
        UserRepositoryPort userRepositoryPort,
        UserProfileRepositoryPort userProfileRepositoryPort,
        ConfirmHoldUseCase confirmHoldUseCase
    ) {
        this.createHoldUseCase = createHoldUseCase;
        this.userRepositoryPort = userRepositoryPort;
        this.userProfileRepositoryPort = userProfileRepositoryPort;
        this.confirmHoldUseCase = confirmHoldUseCase;
    }

    @PostMapping("/holds")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CreateHoldUseCase.HoldCreatedResult> createHold(@RequestBody @Valid HoldRequest request) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("No authenticated user"));

        var user = userRepositoryPort
            .findOneWithAuthoritiesByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));

        var profile = userProfileRepositoryPort
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Profile not found for user: " + login));

        CreateHoldUseCase.HoldCreatedResult result = createHoldUseCase.createHold(
            request.professionalId(),
            request.date(),
            request.startTime(),
            user,
            profile.getExternalPatientId()
        );

        return ResponseEntity.status(201).body(result);
    }

    @PostMapping("/holds/{holdId}/confirm")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ConfirmHoldUseCase.ConfirmHoldResult> confirmHold(
        @PathVariable Long holdId,
        @RequestBody @Valid ConfirmHoldRequest request
    ) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("No authenticated user"));

        var user = userRepositoryPort
            .findOneWithAuthoritiesByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));

        ConfirmHoldUseCase.ConfirmHoldResult result = confirmHoldUseCase.confirmHold(
            holdId,
            request.patientFirstName(),
            request.patientLastName(),
            user
        );

        return ResponseEntity.status(202).body(result);
    }
}
