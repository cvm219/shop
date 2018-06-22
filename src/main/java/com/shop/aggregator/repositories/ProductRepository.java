package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Product;
import com.shop.aggregator.model.ProductWithPrice;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    @Query("SELECT p, o.price FROM Product p LEFT JOIN p.offers o, CategoryProduct cp WHERE p.status=:status AND p.id=cp.productId AND cp.categoryId=:categoryId")
    List<ProductWithPrice> getProductWithPriceByCategoryAndStatus(@Param("categoryId") UUID categoryId, @Param("status") String status);
    
    @Query("SELECT p, o.price FROM Product p LEFT JOIN p.offers o WHERE p.status=:status")
    List<ProductWithPrice> getProductWithPriceByStatus(@Param("status") String status);
    
}
