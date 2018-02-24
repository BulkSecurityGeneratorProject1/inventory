package com.sonnetstone.inventory.repository.search;

import com.sonnetstone.inventory.domain.SalesDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SalesDetails entity.
 */
public interface SalesDetailsSearchRepository extends ElasticsearchRepository<SalesDetails, Long> {
}
