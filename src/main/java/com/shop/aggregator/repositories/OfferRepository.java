package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Offer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    
    @Query("SELECT o FROM Offer o WHERE o.retailer.id=:retailerId AND o.productId=:productId")
    Offer findByRetailerAndProduct(@Param("retailerId") UUID retailerId, @Param("productId") UUID productId);
    
    @Query("SELECT o FROM Offer o WHERE o.retailer.id=:retailerId")
    List<Offer> findByRetailerId(@Param("retailerId") UUID retailerId);
    
    @Query("SELECT o FROM Offer o WHERE o.productId=:productId")
    List<Offer> findByProductId(@Param("productId") UUID productId);
    
}
