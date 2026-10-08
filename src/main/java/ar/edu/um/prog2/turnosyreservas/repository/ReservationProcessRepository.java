package ar.edu.um.prog2.turnosyreservas.repository;

import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReservationProcess entity.
 */
@Repository
public interface ReservationProcessRepository extends JpaRepository<ReservationProcess, Long> {
    @Query(
        "select reservationProcess from ReservationProcess reservationProcess where reservationProcess.user.login = ?#{authentication.name}"
    )
    List<ReservationProcess> findByUserIsCurrentUser();

    default Optional<ReservationProcess> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReservationProcess> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReservationProcess> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reservationProcess from ReservationProcess reservationProcess left join fetch reservationProcess.user",
        countQuery = "select count(reservationProcess) from ReservationProcess reservationProcess"
    )
    Page<ReservationProcess> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reservationProcess from ReservationProcess reservationProcess left join fetch reservationProcess.user")
    List<ReservationProcess> findAllWithToOneRelationships();

    @Query(
        "select reservationProcess from ReservationProcess reservationProcess left join fetch reservationProcess.user where reservationProcess.id =:id"
    )
    Optional<ReservationProcess> findOneWithToOneRelationships(@Param("id") Long id);
}
