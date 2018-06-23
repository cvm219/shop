package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Retailer;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, UUID> {
    
    public static final Sort DEFAULT_SORT = new Sort(Sort.Direction.ASC, "name");

    @Query("SELECT r FROM Retailer r WHERE r.name=:name")    
    Retailer findByName(@Param("name") String name);
    
}
