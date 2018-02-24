package com.sonnetstone.inventory.repository.search;

import com.sonnetstone.inventory.domain.ProductDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductDetails entity.
 */
public interface ProductDetailsSearchRepository extends ElasticsearchRepository<ProductDetails, Long> {
}
