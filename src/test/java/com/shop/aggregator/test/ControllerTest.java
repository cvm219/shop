package com.shop.aggregator.test;

import com.shop.aggregator.controller.CategoryController;
import com.shop.aggregator.controller.OfferController;
import com.shop.aggregator.controller.ProductController;
import com.shop.aggregator.controller.RetailerController;
import com.shop.aggregator.exceptions.RestException;
import com.shop.aggregator.model.Category;
import com.shop.aggregator.model.Offer;
import com.shop.aggregator.model.Product;
import com.shop.aggregator.model.Retailer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class ControllerTest {
    
    private static final String TEST_CATEGORY_NAME_PREFIX = "-->TEST CATEGORY PREFIX<--";
    private static final String TEST_RETAILER_NAME_PREFIX = "-->TEST RETAILER PREFIX<--";
    private static final String TEST_PRODUCT_NAME_PREFIX = "-->TEST PRODUCT PREFIX<--";
    private static final String UPDATED_SUFFIX = "-->UPDATED<--";
    
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    
    @Autowired
    private CategoryController categoryController;
    
    @Autowired
    private RetailerController retailerController;
    
    @Autowired
    private OfferController offerController;
    
    @Autowired
    private ProductController productController;
    
    private List<Category> initCategories() throws RestException {
        List<Category> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName(TEST_CATEGORY_NAME_PREFIX + " #" + i);
            result.add(category);
            categoryController.create(category);
        }
        return result;
    }
    
    private void checkExistingCategories(List<Category> categories) throws RestException {
        int categoriesFound = 0;
        for (Category controllerCategory : categoryController.getAll()) {
            for (Category category : categories) {
                if (controllerCategory.getId().equals(category.getId()) && controllerCategory.getName().equals(category.getName())) {
                    categoriesFound++;
                }
            }
        }
        assertEquals(categories.size(), categoriesFound);
        for (Category category : categories) {
            Category controllerCategory = categoryController.getById(category.getId());
            assertNotNull(controllerCategory);
            assertEquals(category.getId(), controllerCategory.getId());
            assertEquals(category.getName(), controllerCategory.getName());
        }
    }
    
    private List<Retailer> initRetailers() throws RestException {
        List<Retailer> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Retailer retailer = new Retailer();
            retailer.setName(TEST_RETAILER_NAME_PREFIX + "#" + i);
            result.add(retailer);
            retailerController.create(retailer);
        }
        return result;
    }
    
    private void checkExistingRetailers(List<Retailer> retailers) throws RestException {
        int retailersFound = 0;
        for (Retailer controllerRetailer : retailerController.getAll()) {
            for (Retailer retailer : retailers) {
                if (controllerRetailer.getId().equals(retailer.getId()) && controllerRetailer.getName().equals(retailer.getName())) {
                    retailersFound++;
                }
            }
        }
        assertEquals(retailers.size(), retailersFound);
        for (Retailer retailer : retailers) {
            Retailer controllerRetailer = retailerController.getById(retailer.getId());
            assertNotNull(controllerRetailer);
            assertEquals(retailer.getId(), controllerRetailer.getId());
            assertEquals(retailer.getName(), controllerRetailer.getName());
        }
    }
    
    private List<Product> initProducts() throws RestException {
        List<Product> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setName(TEST_PRODUCT_NAME_PREFIX + "#" + i);
            product.setStatus(Product.Statuses.enabled);
            result.add(product);
            productController.create(product);
        }
        return result;
    }
    
    private void checkExistingProducts(List<Product> products) throws RestException {
        int productsFound = 0;
        for (Product controllerProduct : productController.getAll()) {
            for (Product product : products) {
                if (controllerProduct.getId().equals(product.getId()) && controllerProduct.getName().equals(product.getName()) 
                        && controllerProduct.getStatus().equals(product.getStatus()) && Objects.equals(controllerProduct.getSpecification(), product.getSpecification())) {
                    productsFound++;                    
                }
            }
        }
        assertEquals(products.size(), productsFound);
        for (Product product : products) {
            Product controllerProduct = productController.getById(product.getId());
            assertNotNull(controllerProduct);
            assertEquals(product.getId(), controllerProduct.getId());
            assertEquals(product.getName(), controllerProduct.getName());
            assertEquals(product.getSpecification(), controllerProduct.getSpecification());
            assertEquals(product.getStatus(), controllerProduct.getStatus());
        }
    }
    
    private List<Offer> initOffers(List<Product> products, List<Retailer> retailers) throws RestException {
        List<Offer> result = new ArrayList<>();
        for (Retailer retailer : retailers) {
            for (Product product : products) {
                Offer offer = new Offer();
                offer.setRetailer(retailer);
                offer.setRetailerId(retailer.getId());
                offer.setProductId(product.getId());
                offer.setPrice(RANDOM.nextInt(100) + 1.99);
                result.add(offer);
                offerController.create(offer);
            }
        }
        return result;
    }
    
    private void checkExistingOffers(List<Offer> offers) throws RestException {
        int offersFound = 0;
        for (Offer controllerOffer : offerController.getAll()) {
            for (Offer offer : offers) {
                if (controllerOffer.getId().equals(offer.getId()) && controllerOffer.getRetailer().getId().equals(offer.getRetailerId()) 
                        && controllerOffer.getProductId().equals(offer.getProductId()) && controllerOffer.getPrice().equals(offer.getPrice())) {
                    offersFound++;
                }
            }
        }
        assertEquals(offers.size(), offersFound);
        for (Offer offer : offers) {
            Offer controllerOffer = offerController.getById(offer.getId());
            assertNotNull(controllerOffer);
            assertEquals(offer.getId(), controllerOffer.getId());
            assertEquals(offer.getRetailerId(), controllerOffer.getRetailer().getId());
            assertEquals(offer.getProductId(), controllerOffer.getProductId());
            assertEquals(offer.getPrice(), controllerOffer.getPrice());
        }
    }
    
    @Test
    public void testCategoriesController() throws RestException {
        List<Category> categories = initCategories(); // initialize categories with saving
        checkExistingCategories(categories);
        // trying to create existing category
        for (Category category : categories) {
            try {
                categoryController.create(category);
                assertFalse(true);
            } catch (RestException e) {
                assertEquals(400, e.getStatusCode());
                assertEquals("Category with same name already exists", e.getDescription());
            }
        }
        for (Category category : categories) {
            category.setName(category.getName() + UPDATED_SUFFIX);
            categoryController.update(category, category.getId());
        }
        checkExistingCategories(categories);
        int ind = RANDOM.nextInt(categories.size());
        categoryController.delete(categories.get(ind).getId());
        try {
            categoryController.getById(categories.get(ind).getId());
            assertFalse(true);
        } catch (RestException e) {
            assertEquals(404, e.getStatusCode());
            assertEquals("Category not found", e.getDescription());
        }
    }
    
    @Test
    public void testRetailerController() throws RestException {
        List<Retailer> retailers = initRetailers();
        checkExistingRetailers(retailers);
        // trying to create existing retailer
        for (Retailer retailer : retailers) {
            try {
                retailerController.create(retailer);
                assertFalse(true);
            } catch (RestException e) {
                assertEquals(400, e.getStatusCode());
                assertEquals("Retailer with same name already exists", e.getDescription());
            }
        }
        for (Retailer retailer : retailers) {
            retailer.setName(retailer.getName() + UPDATED_SUFFIX);
            retailerController.update(retailer, retailer.getId());
        }
        checkExistingRetailers(retailers);
        int ind = RANDOM.nextInt(retailers.size());
        retailerController.delete(retailers.get(ind).getId());
        try {
            retailerController.getById(retailers.get(ind).getId());
            assertFalse(true);
        } catch (RestException e) {
            assertEquals(404, e.getStatusCode());
            assertEquals("Retailer not found", e.getDescription());            
        }
    }
    
    @Test
    public void testProductController() throws RestException {
        List<Product> products = initProducts();
        checkExistingProducts(products);
        for (Product product : products) {
            try {
                productController.create(product);
                assertFalse(true);
            } catch (RestException e) {
                assertEquals(400, e.getStatusCode());
                assertEquals("Product with same name already exists", e.getDescription());
            }
        }
        for (Product product : products) {
            product.setName(product.getName() + UPDATED_SUFFIX);
            product.setSpecification(product.getName() + " IS SPECIFICATION");
            productController.update(product, product.getId());
        }
        checkExistingProducts(products);
        int ind = RANDOM.nextInt(products.size());
        productController.delete(products.get(ind).getId());
        try {
            productController.getById(products.get(ind).getId());
            assertFalse(true);
        } catch (RestException e) {
            assertEquals(404, e.getStatusCode());
            assertEquals("Product not found", e.getDescription());
        }
    }
    
    @Test
    public void testOfferController() throws RestException {
        List<Product> products = initProducts();
        List<Retailer> retailers = initRetailers();
        try {
            Offer offer = new Offer();
            offer.setProductId(products.get(0).getId());
            offer.setRetailerId(retailers.get(0).getId());
            offer.setPrice(-10.0);
            offerController.create(offer);
        } catch (RestException e) {
            assertEquals(400, e.getStatusCode());
            assertEquals("Offer price less or equals zero", e.getDescription());
        }
        List<Offer> offers = initOffers(products, retailers);
        checkExistingOffers(offers);
        for (Offer offer : offers) {
            try {
                offerController.create(offer);
                assertFalse(true);
            } catch (RestException e) {
                assertEquals(400, e.getStatusCode());
                assertEquals("Offer already exists", e.getDescription());
            }
        }
        for (Offer offer : offers) {
            offer.setPrice(offer.getPrice() + 10);
            offerController.update(offer, offer.getId());
        }
        checkExistingOffers(offers);
        int ind = RANDOM.nextInt(offers.size());
        offerController.delete(offers.get(ind).getId());
        try {
            offerController.getById(offers.get(ind).getId());
            assertFalse(true);
        } catch (RestException e) {
            assertEquals(404, e.getStatusCode());
            assertEquals("Offer not found", e.getDescription());
        }
    }
    
}
