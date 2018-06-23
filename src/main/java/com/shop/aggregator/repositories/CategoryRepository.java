package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Category;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
    public static final Sort DEFAULT_SORT = new Sort(Sort.Direction.ASC, "name");
    
    @Query("SELECT c FROM Category c WHERE c.name=:name")
    Category findByName(@Param("name") String name);
    
}
