package com.chatbot.nacim.service.impl;

import com.chatbot.nacim.service.DiplomeService;
import com.chatbot.nacim.domain.Diplome;
import com.chatbot.nacim.repository.DiplomeRepository;
import com.chatbot.nacim.service.dto.DiplomeDTO;
import com.chatbot.nacim.service.mapper.DiplomeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Diplome.
 */
@Service
@Transactional
public class DiplomeServiceImpl implements DiplomeService {

    private final Logger log = LoggerFactory.getLogger(DiplomeServiceImpl.class);

    private final DiplomeRepository diplomeRepository;

    private final DiplomeMapper diplomeMapper;

    public DiplomeServiceImpl(DiplomeRepository diplomeRepository, DiplomeMapper diplomeMapper) {
        this.diplomeRepository = diplomeRepository;
        this.diplomeMapper = diplomeMapper;
    }

    /**
     * Save a diplome.
     *
     * @param diplomeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DiplomeDTO save(DiplomeDTO diplomeDTO) {
        log.debug("Request to save Diplome : {}", diplomeDTO);
        Diplome diplome = diplomeMapper.toEntity(diplomeDTO);
        diplome = diplomeRepository.save(diplome);
        return diplomeMapper.toDto(diplome);
    }

    /**
     * Get all the diplomes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DiplomeDTO> findAll() {
        log.debug("Request to get all Diplomes");
        return diplomeRepository.findAll().stream()
            .map(diplomeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one diplome by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DiplomeDTO> findOne(Long id) {
        log.debug("Request to get Diplome : {}", id);
        return diplomeRepository.findById(id)
            .map(diplomeMapper::toDto);
    }

    /**
     * Delete the diplome by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Diplome : {}", id);
        diplomeRepository.deleteById(id);
    }
}
