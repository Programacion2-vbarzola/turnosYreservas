import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.domain.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.domain.port.out.HoldRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.repository.HoldRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class HoldPersistenceAdapter implements HoldRepositoryPort {

    private final HoldRepository holdRepository;

    HoldPersistenceAdapter(HoldRepository holdRepository) {
        this.holdRepository = holdRepository;
    }

    @Override
    public Hold save(Hold hold) {
        return holdRepository.save(hold);
    }

    @Override
    public boolean existsActiveHoldForSlot(Long professionalId, LocalDate date, LocalTime startTime) {
        return holdRepository.existsByProfessionalIdAndDateAndStartTimeAndStatus(
            professionalId,
            date,
            startTime.toString(),
            HoldStatus.HELD
        );
    }
}
