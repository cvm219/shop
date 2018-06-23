package com.shop.aggregator.controller;

import com.shop.aggregator.exceptions.RestException;
import com.shop.aggregator.model.Offer;
import com.shop.aggregator.repositories.OfferRepository;
import com.shop.aggregator.repositories.ProductRepository;
import com.shop.aggregator.repositories.RetailerRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/offer")
public class OfferController {
    
    @Autowired
    private OfferRepository offerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private RetailerRepository retailerRepository;
    
    private void validate(Offer offer) throws RestException {
        if (offer.getProductId() == null) {
            throw new RestException(400, "Product id not defined");
        }
        if (!productRepository.exists(offer.getProductId())) {
            throw new RestException(404, "Product not exists");
        }
        if (offer.getRetailerId() == null) {
            throw new RestException(400, "Retailer id not defined");
        }
        offer.setRetailer(retailerRepository.findOne(offer.getRetailerId()));
        if (offer.getRetailer() == null) {
            throw new RestException(404, "Retailer not exists");
        }
        if (offer.getPrice() == null || offer.getPrice() <= 0) {
            throw new RestException(404, "Offer price less or equals zero");
        }
    }
    
    @GetMapping
    @ResponseBody
    public List<Offer> getAll() throws RestException {
        return offerRepository.findAll();
    }
    
    @GetMapping("/{offerId}")
    @ResponseBody
    public Offer getById(@PathVariable("offerId") UUID offerId) throws RestException {
        Offer offer = offerRepository.findOne(offerId);
        if (offer == null) {
            throw new RestException(404, "Offer not found");
        }
        return offer;
    }
    
    @PostMapping
    @ResponseBody
    public String create(@RequestBody Offer offer) throws RestException {
        validate(offer);
        if (offerRepository.findByRetailerAndProduct(offer.getRetailer().getId(), offer.getProductId()) != null) {
            throw new RestException(400, "Offer already exists");
        }
        offerRepository.save(offer);
        return "Saved";
    }
    
    @PutMapping("/{offerId}")
    @ResponseBody
    public String update(@RequestBody Offer offer, @PathVariable("offerId") UUID offerId) throws RestException {
        if (offer.getPrice() == null || offer.getPrice() <= 0) {
            throw new RestException(404, "Offer price less or equals zero");            
        }
        Offer entity = offerRepository.findOne(offerId);
        if (entity == null) {
            throw new RestException(404, "Offer not found");
        }
        entity.setPrice(offer.getPrice());
        offerRepository.save(entity);
        return "Updated";
    }
    
    @DeleteMapping("/{offerId}")
    @ResponseBody
    public String delete(@PathVariable("offerId") UUID offerId) throws RestException {
        if (!offerRepository.exists(offerId)) {
            throw new RestException(404, "Offer not found");
        }
        offerRepository.delete(offerId);
        return "Deleted";
    }

}
