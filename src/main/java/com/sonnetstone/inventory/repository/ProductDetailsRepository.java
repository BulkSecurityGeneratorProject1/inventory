package com.sonnetstone.inventory.repository;

import com.sonnetstone.inventory.domain.ProductDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProductDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {

}
