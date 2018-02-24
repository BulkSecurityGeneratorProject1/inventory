package com.sonnetstone.inventory.repository.search;

import com.sonnetstone.inventory.domain.WasteDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WasteDetails entity.
 */
public interface WasteDetailsSearchRepository extends ElasticsearchRepository<WasteDetails, Long> {
}
