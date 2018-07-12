package com.simpleeffect.trucks.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Cost.
 */
@Entity
@Table(name = "cost")
public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "jhi_time")
    private ZonedDateTime time;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Truck truck;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Cost location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Cost time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Float getAmount() {
        return amount;
    }

    public Cost amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public Cost type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public Cost description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Truck getTruck() {
        return truck;
    }

    public Cost truck(Truck truck) {
        this.truck = truck;
        return this;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
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
        Cost cost = (Cost) o;
        if (cost.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cost.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cost{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", time='" + getTime() + "'" +
            ", amount=" + getAmount() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
