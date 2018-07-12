package com.simpleeffect.trucks.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Truck.
 */
@Entity
@Table(name = "truck")
public class Truck implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver")
    private String driver;

    @Column(name = "registration")
    private String registration;

    @Column(name = "jhi_type")
    private String type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public Truck driver(String driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRegistration() {
        return registration;
    }

    public Truck registration(String registration) {
        this.registration = registration;
        return this;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getType() {
        return type;
    }

    public Truck type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
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
        Truck truck = (Truck) o;
        if (truck.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), truck.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Truck{" +
            "id=" + getId() +
            ", driver='" + getDriver() + "'" +
            ", registration='" + getRegistration() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
