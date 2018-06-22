package com.shop.aggregator.model;

import javax.persistence.Entity;

@Entity
public class ProductWithPrice extends Product {

    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
}
