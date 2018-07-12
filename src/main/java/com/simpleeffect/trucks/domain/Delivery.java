package com.simpleeffect.trucks.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Delivery.
 */
@Entity
@Table(name = "delivery")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "jhi_size")
    private Long size;

    @Column(name = "weight")
    private Long weight;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Column(name = "pickup_description")
    private String pickupDescription;

    @Column(name = "delivery_description")
    private String deliveryDescription;

    @Column(name = "status")
    private String status;

    @Column(name = "expected_income")
    private Float expectedIncome;

    @Column(name = "pickup_date")
    private ZonedDateTime pickupDate;

    @Column(name = "delivery_date")
    private ZonedDateTime deliveryDate;

    @ManyToOne
    private Truck truck;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public Delivery dateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getSize() {
        return size;
    }

    public Delivery size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getWeight() {
        return weight;
    }

    public Delivery weight(Long weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public Delivery pickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
        return this;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public Delivery deliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
        return this;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getPickupDescription() {
        return pickupDescription;
    }

    public Delivery pickupDescription(String pickupDescription) {
        this.pickupDescription = pickupDescription;
        return this;
    }

    public void setPickupDescription(String pickupDescription) {
        this.pickupDescription = pickupDescription;
    }

    public String getDeliveryDescription() {
        return deliveryDescription;
    }

    public Delivery deliveryDescription(String deliveryDescription) {
        this.deliveryDescription = deliveryDescription;
        return this;
    }

    public void setDeliveryDescription(String deliveryDescription) {
        this.deliveryDescription = deliveryDescription;
    }

    public String getStatus() {
        return status;
    }

    public Delivery status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getExpectedIncome() {
        return expectedIncome;
    }

    public Delivery expectedIncome(Float expectedIncome) {
        this.expectedIncome = expectedIncome;
        return this;
    }

    public void setExpectedIncome(Float expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

    public ZonedDateTime getPickupDate() {
        return pickupDate;
    }

    public Delivery pickupDate(ZonedDateTime pickupDate) {
        this.pickupDate = pickupDate;
        return this;
    }

    public void setPickupDate(ZonedDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public Delivery deliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Truck getTruck() {
        return truck;
    }

    public Delivery truck(Truck truck) {
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
        Delivery delivery = (Delivery) o;
        if (delivery.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), delivery.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Delivery{" +
            "id=" + getId() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", size=" + getSize() +
            ", weight=" + getWeight() +
            ", pickupLocation='" + getPickupLocation() + "'" +
            ", deliveryLocation='" + getDeliveryLocation() + "'" +
            ", pickupDescription='" + getPickupDescription() + "'" +
            ", deliveryDescription='" + getDeliveryDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", expectedIncome=" + getExpectedIncome() +
            ", pickupDate='" + getPickupDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            "}";
    }
}
