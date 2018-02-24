package com.sonnetstone.inventory.repository;

import com.sonnetstone.inventory.domain.WasteDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WasteDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WasteDetailsRepository extends JpaRepository<WasteDetails, Long> {

}
