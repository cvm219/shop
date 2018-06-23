package com.shop.aggregator.controller;

import com.shop.aggregator.exceptions.RestException;
import com.shop.aggregator.model.CategoryProduct;
import com.shop.aggregator.model.Product;
import com.shop.aggregator.model.ProductWithPrice;
import com.shop.aggregator.repositories.CategoryProductRepository;
import com.shop.aggregator.repositories.CategoryRepository;
import com.shop.aggregator.repositories.ProductRepository;
import com.shop.aggregator.repositories.ProductWithPriceRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CategoryProductRepository categoryProductRepository;
    
    @Autowired
    private ProductWithPriceRepository productWithPriceRepository;
    
    private void validateProduct(Product product) throws RestException {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new RestException(400, "Product name not defined");
        }
        if (product.getStatus() == null) {
            throw new RestException(400, "Product status not defined");
        }
    }
    
    @GetMapping
    @ResponseBody
    public List<Product> getAll() {
        return productRepository.findAll(ProductRepository.DEFAULT_SORT);
    }
    
    @GetMapping("/{productId}")
    @ResponseBody
    public Product getById(@PathVariable("productId") UUID productId) throws RestException {
        Product product = productRepository.findOne(productId);
        if (product == null) {
            throw new RestException(404, "Product not found");
        }
        return product;
    }
    
    @PostMapping
    @ResponseBody
    public String create(@RequestBody Product product) throws RestException {
        validateProduct(product);
        if (productRepository.findByName(product.getName()) != null) {
            throw new RestException(400, "Product with same name already exists");
        }
        productRepository.save(product);
        return "Saved";
    }
    
    @PutMapping("/{productId}")
    @ResponseBody
    public String update(@RequestBody Product product, @PathVariable("productId") UUID productId) throws RestException {
        Product entity = productRepository.findOne(productId);
        if (entity == null) {
            throw new RestException(404, "Product not found");
        }
        boolean needUpdate = false;
        if (product.getName() != null && !product.getName().isEmpty()) {
            Product productWithSameName = productRepository.findByName(product.getName());
            if (productWithSameName != null && !productWithSameName.getId().equals(entity.getId())) {
                throw new RestException(400, "Product with same name already exists");
            }
            entity.setName(product.getName());
            needUpdate = true;
        }
        if (product.getSpecification() != null && !product.getSpecification().isEmpty()) {
            entity.setSpecification(product.getSpecification());
            needUpdate = true;
        }
        if (product.getStatus() != null && !product.getStatus().equals(entity.getStatus())) {
            entity.setStatus(product.getStatus());
            needUpdate = true;
        }
        if (needUpdate) {
            productRepository.save(entity);
        }
        return "Updated";
    }
    
    @DeleteMapping("/{productId}")
    @ResponseBody
    public String delete(@PathVariable("productId") UUID productId) throws RestException {
        if (!productRepository.exists(productId)) {
            throw new RestException(404, "Product not found");
        }
        categoryProductRepository.delete(categoryProductRepository.findByProduct(productId));
        productRepository.delete(productId);
        return "Deleted";
    }
    
    @PostMapping("/{productId}/bind/{categoryId}")
    @ResponseBody
    public String bindProductToCategory(@PathVariable("productId") UUID productId, @PathVariable("categoryId") UUID categoryId) throws RestException {
        if (!productRepository.exists(productId)) {
            throw new RestException(404, "Product not found");
        }
        if (!categoryRepository.exists(categoryId)) {
            throw new RestException(404, "Category not found");
        }
        CategoryProduct cp = categoryProductRepository.findByCategoryAndProduct(categoryId, productId);
        if (cp != null) {
            return "Product already bound to category";
        }
        cp = new CategoryProduct();
        cp.setCategoryId(categoryId);
        cp.setProductId(productId);
        categoryProductRepository.save(cp);
        return "Bound";
    }
    
    @PostMapping("/{productId}/unbind/{categoryId}")
    @ResponseBody
    public String unbindProductFromCategory(@PathVariable("productId") UUID productId, @PathVariable("categoryId") UUID categoryId) throws RestException {
        if (!productRepository.exists(productId)) {
            throw new RestException(404, "Product not found");
        }
        if (!categoryRepository.exists(categoryId)) {
            throw new RestException(404, "Category not found");
        }
        CategoryProduct cp = categoryProductRepository.findByCategoryAndProduct(categoryId, productId);
        if (cp == null) {
            return "Product not bound to category";
        }
        categoryProductRepository.delete(cp);
        return "Unbound";
    }
    
    @GetMapping("/withPrice")
    @ResponseBody
    public List<ProductWithPrice> getWithPriceByStatus(@RequestParam(name = "categoryId", required = false) UUID categoryId) {
        return categoryId != null ? productWithPriceRepository.findByCategoryAndStatus(categoryId, Product.Statuses.enabled) : productWithPriceRepository.findByStatus(Product.Statuses.enabled);
    }

}
