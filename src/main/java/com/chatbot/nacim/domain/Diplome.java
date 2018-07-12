package com.chatbot.nacim.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Diplome.
 */
@Entity
@Table(name = "diplome")
public class Diplome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "intitule", nullable = false)
    private String intitule;

    @NotNull
    @Column(name = "obtaining_date", nullable = false)
    private LocalDate obtainingDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public Diplome intitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public LocalDate getObtainingDate() {
        return obtainingDate;
    }

    public Diplome obtainingDate(LocalDate obtainingDate) {
        this.obtainingDate = obtainingDate;
        return this;
    }

    public void setObtainingDate(LocalDate obtainingDate) {
        this.obtainingDate = obtainingDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diplome diplome = (Diplome) o;
        if (diplome.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), diplome.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Diplome{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", obtainingDate='" + getObtainingDate() + "'" +
            "}";
    }
}
