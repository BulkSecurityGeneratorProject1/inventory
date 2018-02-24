package com.sonnetstone.inventory.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductDetails.
 */
@Entity
@Table(name = "product_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productdetails")
public class ProductDetails implements Serializable {

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

    @NotNull
    @Size(max = 50)
    @Column(name = "sold", length = 50, nullable = false)
    private String sold;

    @ManyToOne
    private Product productDetail;

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

    public ProductDetails length(Double length) {
        this.length = length;
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getBreadth() {
        return breadth;
    }

    public ProductDetails breadth(Double breadth) {
        this.breadth = breadth;
        return this;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getSquareFeet() {
        return squareFeet;
    }

    public ProductDetails squareFeet(Double squareFeet) {
        this.squareFeet = squareFeet;
        return this;
    }

    public void setSquareFeet(Double squareFeet) {
        this.squareFeet = squareFeet;
    }

    public String getSold() {
        return sold;
    }

    public ProductDetails sold(String sold) {
        this.sold = sold;
        return this;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public Product getProductDetail() {
        return productDetail;
    }

    public ProductDetails productDetail(Product product) {
        this.productDetail = product;
        return this;
    }

    public void setProductDetail(Product product) {
        this.productDetail = product;
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
        ProductDetails productDetails = (ProductDetails) o;
        if (productDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
            "id=" + getId() +
            ", length=" + getLength() +
            ", breadth=" + getBreadth() +
            ", squareFeet=" + getSquareFeet() +
            ", sold='" + getSold() + "'" +
            "}";
    }
}
