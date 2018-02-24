package com.sonnetstone.inventory.repository.search;

import com.sonnetstone.inventory.domain.LeftoverDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LeftoverDetails entity.
 */
public interface LeftoverDetailsSearchRepository extends ElasticsearchRepository<LeftoverDetails, Long> {
}
