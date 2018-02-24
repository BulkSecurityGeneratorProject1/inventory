package com.sonnetstone.inventory.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WasteDetails.
 */
@Entity
@Table(name = "waste_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "wastedetails")
public class WasteDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "length", nullable = false)
    private Double length;

    @NotNull
    @Column(name = "breadth", nullable = false)
    private Double breadth;

    @NotNull
    @Column(name = "square_feet", nullable = false)
    private Double squareFeet;

    @ManyToOne
    private ProductDetails wasteDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLength() {
        return length;
    }

    public WasteDetails length(Double length) {
        this.length = length;
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getBreadth() {
        return breadth;
    }

    public WasteDetails breadth(Double breadth) {
        this.breadth = breadth;
        return this;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getSquareFeet() {
        return squareFeet;
    }

    public WasteDetails squareFeet(Double squareFeet) {
        this.squareFeet = squareFeet;
        return this;
    }

    public void setSquareFeet(Double squareFeet) {
        this.squareFeet = squareFeet;
    }

    public ProductDetails getWasteDetail() {
        return wasteDetail;
    }

    public WasteDetails wasteDetail(ProductDetails productDetails) {
        this.wasteDetail = productDetails;
        return this;
    }

    public void setWasteDetail(ProductDetails productDetails) {
        this.wasteDetail = productDetails;
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
        WasteDetails wasteDetails = (WasteDetails) o;
        if (wasteDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wasteDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WasteDetails{" +
            "id=" + getId() +
            ", length=" + getLength() +
            ", breadth=" + getBreadth() +
            ", squareFeet=" + getSquareFeet() +
            "}";
    }
}
