package com.shop.aggregator.repositories.impl;

import com.shop.aggregator.model.CategoryProduct;
import com.shop.aggregator.model.Offer;
import com.shop.aggregator.model.Product;
import com.shop.aggregator.model.ProductWithPrice;
import com.shop.aggregator.repositories.ProductWithPriceRepository;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProductWithPriceRepositoryImpl implements ProductWithPriceRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductWithPrice> findByStatus(Product.Statuses status) {
        return em.createNativeQuery("SELECT p.*, min(o.price) AS price FROM "+ Product.TABLE_NAME + " p LEFT JOIN " + Offer.TABLE_NAME + " o ON o.product_id=p.id WHERE p.status=:status GROUP BY p.id ORDER BY p.name", ProductWithPrice.class)
                .setParameter("status", status.name())
                .getResultList();
    }

    @Override
    public List<ProductWithPrice> findByCategoryAndStatus(UUID categoryId, Product.Statuses status) {
        return em.createNativeQuery("SELECT p.*, min(o.price) AS price FROM " + Product.TABLE_NAME + " p LEFT JOIN " + Offer.TABLE_NAME + " o ON o.product_id=p.id, " + CategoryProduct.TABLE_NAME + " cp WHERE p.status=:status AND p.id=cp.product_id AND cp.category_id=:categoryId GROUP BY p.id ORDER BY p.name", ProductWithPrice.class)
                .setParameter("categoryId", categoryId)
                .setParameter("status", status.name())
                .getResultList();
    }

}
