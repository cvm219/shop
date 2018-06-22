package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Retailer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, UUID> {
    
}
