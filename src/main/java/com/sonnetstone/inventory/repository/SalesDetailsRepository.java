package com.sonnetstone.inventory.repository;

import com.sonnetstone.inventory.domain.SalesDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SalesDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesDetailsRepository extends JpaRepository<SalesDetails, Long> {

}
