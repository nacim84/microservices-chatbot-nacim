package com.chatbot.nacim.service.mapper;

import com.chatbot.nacim.domain.*;
import com.chatbot.nacim.service.dto.DiplomeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Diplome and its DTO DiplomeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DiplomeMapper extends EntityMapper<DiplomeDTO, Diplome> {



    default Diplome fromId(Long id) {
        if (id == null) {
            return null;
        }
        Diplome diplome = new Diplome();
        diplome.setId(id);
        return diplome;
    }
}
