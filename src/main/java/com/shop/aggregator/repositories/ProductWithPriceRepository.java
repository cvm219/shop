package com.shop.aggregator.repositories;

import com.shop.aggregator.model.Product;
import com.shop.aggregator.model.ProductWithPrice;
import java.util.List;
import java.util.UUID;

public interface ProductWithPriceRepository {
    
    List<ProductWithPrice> findByStatus(Product.Statuses status);
    
    List<ProductWithPrice> findByCategoryAndStatus(UUID categoryId, Product.Statuses status);
    
}
