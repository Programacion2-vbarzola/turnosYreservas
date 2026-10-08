package ar.edu.um.prog2.turnosyreservas.service.impl;

import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.repository.HoldRepository;
import ar.edu.um.prog2.turnosyreservas.service.HoldService;
import ar.edu.um.prog2.turnosyreservas.service.dto.HoldDTO;
import ar.edu.um.prog2.turnosyreservas.service.mapper.HoldMapper;
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
 * Service Implementation for managing {@link ar.edu.um.prog2.turnosyreservas.domain.Hold}.
 */
@Service
@Transactional
public class HoldServiceImpl implements HoldService {

    private static final Logger LOG = LoggerFactory.getLogger(HoldServiceImpl.class);

    private final HoldRepository holdRepository;

    private final HoldMapper holdMapper;

    public HoldServiceImpl(HoldRepository holdRepository, HoldMapper holdMapper) {
        this.holdRepository = holdRepository;
        this.holdMapper = holdMapper;
    }

    @Override
    public HoldDTO save(HoldDTO holdDTO) {
        LOG.debug("Request to save Hold : {}", holdDTO);
        Hold hold = holdMapper.toEntity(holdDTO);
        hold = holdRepository.save(hold);
        return holdMapper.toDto(hold);
    }

    @Override
    public HoldDTO update(HoldDTO holdDTO) {
        LOG.debug("Request to update Hold : {}", holdDTO);
        Hold hold = holdMapper.toEntity(holdDTO);
        hold = holdRepository.save(hold);
        return holdMapper.toDto(hold);
    }

    @Override
    public Optional<HoldDTO> partialUpdate(HoldDTO holdDTO) {
        LOG.debug("Request to partially update Hold : {}", holdDTO);

        return holdRepository
            .findById(holdDTO.getId())
            .map(existingHold -> {
                holdMapper.partialUpdate(existingHold, holdDTO);

                return existingHold;
            })
            .map(holdRepository::save)
            .map(holdMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoldDTO> findAll() {
        LOG.debug("Request to get all Holds");
        return holdRepository.findAll().stream().map(holdMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<HoldDTO> findAllWithEagerRelationships(Pageable pageable) {
        return holdRepository.findAllWithEagerRelationships(pageable).map(holdMapper::toDto);
    }

    /**
     *  Get all the holds where ReservationProcess is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HoldDTO> findAllWhereReservationProcessIsNull() {
        LOG.debug("Request to get all holds where ReservationProcess is null");
        return StreamSupport.stream(holdRepository.findAll().spliterator(), false)
            .filter(hold -> hold.getReservationProcess() == null)
            .map(holdMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoldDTO> findOne(Long id) {
        LOG.debug("Request to get Hold : {}", id);
        return holdRepository.findOneWithEagerRelationships(id).map(holdMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Hold : {}", id);
        holdRepository.deleteById(id);
    }
}
