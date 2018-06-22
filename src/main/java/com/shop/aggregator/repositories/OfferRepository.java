package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Offer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    
}
