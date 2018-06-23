package com.shop.aggregator.repositories;

import com.shop.aggregator.model.CategoryProduct;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, UUID> {
    
    @Query("SELECT cp FROM CategoryProduct cp WHERE cp.categoryId=:categoryId AND cp.productId=:productId")
    CategoryProduct findByCategoryAndProduct(@Param("categoryId") UUID categoryId, @Param("productId") UUID productId);
    
    @Query("SELECT cp FROM CategoryProduct cp WHERE cp.categoryId=:categoryId")
    List<CategoryProduct> findByCategory(@Param("categoryId") UUID categoryId);
    
    @Query("SELECT cp FROM CategoryProduct cp WHERE cp.productId=:productId")
    List<CategoryProduct> findByProduct(@Param("productId") UUID productId);
        
}
