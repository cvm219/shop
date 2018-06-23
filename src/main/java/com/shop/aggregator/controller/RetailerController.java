package com.shop.aggregator.controller;

import com.shop.aggregator.exceptions.RestException;
import com.shop.aggregator.model.Retailer;
import com.shop.aggregator.repositories.OfferRepository;
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
@RequestMapping("/retailer")
public class RetailerController {

    @Autowired
    private RetailerRepository retailerRepository;
    
    @Autowired
    private OfferRepository offerRepository;
    
    private void validateRetailer(Retailer retailer) throws RestException {
        if (retailer.getName() == null || retailer.getName().isEmpty()) {
            throw new RestException(400, "Retailer name not defined");
        }
    }
    
    @GetMapping
    @ResponseBody
    public List<Retailer> getAll() {
        return retailerRepository.findAll(RetailerRepository.DEFAULT_SORT);
    }
    
    @GetMapping("/{retailerId}")
    @ResponseBody
    public Retailer getById(@PathVariable("retailerId") UUID retailerId) throws RestException {
        Retailer retailer = retailerRepository.findOne(retailerId);
        if (retailer == null) {
            throw new RestException(404, "Retailer not found");
        }
        return retailer;
    }
    
    @PostMapping
    @ResponseBody
    public String create(@RequestBody Retailer retailer) throws RestException {
        validateRetailer(retailer);
        if (retailerRepository.findByName(retailer.getName()) == null) {
            retailerRepository.save(retailer);
            return "Saved";
        } else {
            throw new RestException(400, "Retailer with same name already exists");
        }
    }
    
    @PutMapping("/{retailerId}")
    @ResponseBody
    public String update(@RequestBody Retailer retailer, @PathVariable("retailerId") UUID retailerId) throws RestException {
        validateRetailer(retailer);
        Retailer entity = retailerRepository.findOne(retailerId);
        if (entity == null) {
            throw new RestException(404, "Retailer not found");
        }
        Retailer retailerWithSameName = retailerRepository.findByName(retailer.getName());
        if (retailerWithSameName != null && !entity.getId().equals(retailerWithSameName.getId())) {
            throw new RestException(400, "Retailer with same name alredy exists");
        }
        entity.setName(retailer.getName());
        retailerRepository.save(entity);
        return "Updated";
    }
    
    @DeleteMapping("/{retailerId}")
    @ResponseBody
    public String delete(@PathVariable("retailerId") UUID retailerId) throws RestException {
        if (!retailerRepository.exists(retailerId)) {
            throw new RestException(404, "Retailer not found");
        }
        offerRepository.delete(offerRepository.findByRetailerId(retailerId));
        retailerRepository.delete(retailerId);
        return "Deleted";
    }
    
}
