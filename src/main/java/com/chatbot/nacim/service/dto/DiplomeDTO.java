package com.chatbot.nacim.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Diplome entity.
 */
public class DiplomeDTO implements Serializable {

    private Long id;

    @NotNull
    private String intitule;

    @NotNull
    private LocalDate obtainingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public LocalDate getObtainingDate() {
        return obtainingDate;
    }

    public void setObtainingDate(LocalDate obtainingDate) {
        this.obtainingDate = obtainingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiplomeDTO diplomeDTO = (DiplomeDTO) o;
        if (diplomeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), diplomeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiplomeDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", obtainingDate='" + getObtainingDate() + "'" +
            "}";
    }
}
