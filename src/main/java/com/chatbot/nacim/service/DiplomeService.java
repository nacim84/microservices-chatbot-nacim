package com.chatbot.nacim.service;

import com.chatbot.nacim.service.dto.DiplomeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Diplome.
 */
public interface DiplomeService {

    /**
     * Save a diplome.
     *
     * @param diplomeDTO the entity to save
     * @return the persisted entity
     */
    DiplomeDTO save(DiplomeDTO diplomeDTO);

    /**
     * Get all the diplomes.
     *
     * @return the list of entities
     */
    List<DiplomeDTO> findAll();


    /**
     * Get the "id" diplome.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DiplomeDTO> findOne(Long id);

    /**
     * Delete the "id" diplome.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
