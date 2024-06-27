package com.nothing.ecommerce.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.nothing.ecommerce.entity.Product;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findByName(String name);

    @Query("{\"fuzzy\": {\"name\": {\"value\": \"?name\", \"fuzziness\": 2}}}")
    List<Product> findByNameFuzzy(String name);

    Product findByUserIdAndName(int userId, String name);

    List<Product> findByUserId(int userId);
}
