package ar.edu.um.prog2.turnosyreservas.service.impl;

import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.repository.ReservationProcessRepository;
import ar.edu.um.prog2.turnosyreservas.service.ReservationProcessService;
import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationProcessDTO;
import ar.edu.um.prog2.turnosyreservas.service.mapper.ReservationProcessMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess}.
 */
@Service
@Transactional
public class ReservationProcessServiceImpl implements ReservationProcessService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationProcessServiceImpl.class);

    private final ReservationProcessRepository reservationProcessRepository;

    private final ReservationProcessMapper reservationProcessMapper;

    public ReservationProcessServiceImpl(
        ReservationProcessRepository reservationProcessRepository,
        ReservationProcessMapper reservationProcessMapper
    ) {
        this.reservationProcessRepository = reservationProcessRepository;
        this.reservationProcessMapper = reservationProcessMapper;
    }

    @Override
    public ReservationProcessDTO save(ReservationProcessDTO reservationProcessDTO) {
        LOG.debug("Request to save ReservationProcess : {}", reservationProcessDTO);
        ReservationProcess reservationProcess = reservationProcessMapper.toEntity(reservationProcessDTO);
        reservationProcess = reservationProcessRepository.save(reservationProcess);
        return reservationProcessMapper.toDto(reservationProcess);
    }

    @Override
    public ReservationProcessDTO update(ReservationProcessDTO reservationProcessDTO) {
        LOG.debug("Request to update ReservationProcess : {}", reservationProcessDTO);
        ReservationProcess reservationProcess = reservationProcessMapper.toEntity(reservationProcessDTO);
        reservationProcess = reservationProcessRepository.save(reservationProcess);
        return reservationProcessMapper.toDto(reservationProcess);
    }

    @Override
    public Optional<ReservationProcessDTO> partialUpdate(ReservationProcessDTO reservationProcessDTO) {
        LOG.debug("Request to partially update ReservationProcess : {}", reservationProcessDTO);

        return reservationProcessRepository
            .findById(reservationProcessDTO.getId())
            .map(existingReservationProcess -> {
                reservationProcessMapper.partialUpdate(existingReservationProcess, reservationProcessDTO);

                return existingReservationProcess;
            })
            .map(reservationProcessRepository::save)
            .map(reservationProcessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationProcessDTO> findAll() {
        LOG.debug("Request to get all ReservationProcesses");
        return reservationProcessRepository
            .findAll()
            .stream()
            .map(reservationProcessMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ReservationProcessDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reservationProcessRepository.findAllWithEagerRelationships(pageable).map(reservationProcessMapper::toDto);
    }

    /**
     *  Get all the reservationProcesses where Reservation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReservationProcessDTO> findAllWhereReservationIsNull() {
        LOG.debug("Request to get all reservationProcesses where Reservation is null");
        return StreamSupport.stream(reservationProcessRepository.findAll().spliterator(), false)
            .filter(reservationProcess -> reservationProcess.getReservation() == null)
            .map(reservationProcessMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationProcessDTO> findOne(Long id) {
        LOG.debug("Request to get ReservationProcess : {}", id);
        return reservationProcessRepository.findOneWithEagerRelationships(id).map(reservationProcessMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReservationProcess : {}", id);
        reservationProcessRepository.deleteById(id);
    }
}
