package com.sonnetstone.inventory.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LeftoverDetails.
 */
@Entity
@Table(name = "leftover_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "leftoverdetails")
public class LeftoverDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "parent_id", nullable = false)
    private Integer parentId;

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
    private ProductDetails leftoverDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public LeftoverDetails parentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Double getLength() {
        return length;
    }

    public LeftoverDetails length(Double length) {
        this.length = length;
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getBreadth() {
        return breadth;
    }

    public LeftoverDetails breadth(Double breadth) {
        this.breadth = breadth;
        return this;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getSquareFeet() {
        return squareFeet;
    }

    public LeftoverDetails squareFeet(Double squareFeet) {
        this.squareFeet = squareFeet;
        return this;
    }

    public void setSquareFeet(Double squareFeet) {
        this.squareFeet = squareFeet;
    }

    public ProductDetails getLeftoverDetail() {
        return leftoverDetail;
    }

    public LeftoverDetails leftoverDetail(ProductDetails productDetails) {
        this.leftoverDetail = productDetails;
        return this;
    }

    public void setLeftoverDetail(ProductDetails productDetails) {
        this.leftoverDetail = productDetails;
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
        LeftoverDetails leftoverDetails = (LeftoverDetails) o;
        if (leftoverDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leftoverDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeftoverDetails{" +
            "id=" + getId() +
            ", parentId=" + getParentId() +
            ", length=" + getLength() +
            ", breadth=" + getBreadth() +
            ", squareFeet=" + getSquareFeet() +
            "}";
    }
}
