package com.sonnetstone.inventory.repository;

import com.sonnetstone.inventory.domain.LeftoverDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LeftoverDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeftoverDetailsRepository extends JpaRepository<LeftoverDetails, Long> {

}
