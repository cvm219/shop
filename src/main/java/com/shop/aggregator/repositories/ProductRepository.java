package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Product;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    public static final Sort DEFAULT_SORT = new Sort(Sort.Direction.ASC, "name");
    
    @Query("SELECT p FROM Product p WHERE p.name=:name")
    Product findByName(@Param("name") String name);
    
}
