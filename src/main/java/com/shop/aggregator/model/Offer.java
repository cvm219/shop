package com.shop.aggregator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = Offer.TABLE_NAME)
public class Offer implements Serializable {
    
    public static final String TABLE_NAME = "offers";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    
    @JsonIgnore
    private transient UUID retailerId;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "retailer_id", referencedColumnName = "id")
    private Retailer retailer;
    
    @Column(name = "product_id", nullable = false)
    private UUID productId;
    
    @Column(name = "price", nullable = false)
    private Double price;
    
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedAt;
    
    @PrePersist
    private void prePersist() {
        createdAt = updatedAt = new Date();
    }
    
    @PreUpdate
    private void preUpdate() {
        updatedAt = new Date();
    }
    
    public UUID getId() {
        return id;
    }

    public UUID getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(UUID retailerId) {
        this.retailerId = retailerId;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
    
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

}
