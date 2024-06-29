package com.nothing.ecommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.exception.ImageException;
import com.nothing.ecommerce.exception.InvalidJWTHeaderException;
import com.nothing.ecommerce.exception.ProductException;
import com.nothing.ecommerce.exception.UnknownErrorException;
import com.nothing.ecommerce.exception.UserException;
import com.nothing.ecommerce.model.ProductInputModel;
import com.nothing.ecommerce.model.ProductUpdateModel;
import com.nothing.ecommerce.model.ProductViewModel;
import com.nothing.ecommerce.services.JWTService;
import com.nothing.ecommerce.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    public ProductViewModel create(@RequestHeader("Authorization") String jwtHeader,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("stock") int stock,
            @RequestParam("category") String category,
            @RequestParam("active") boolean active) {

        if (jwtService.verifyJwtHeader(jwtHeader)) {

            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                ProductInputModel model = new ProductInputModel(name, description, price,
                        stock, category, active);

                if (images.get(0).isEmpty()) {
                    images = null;
                }

                return productService.save(reference, model, images);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (ImageException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping
    public List<ProductViewModel> getProductsByReference(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return productService.getProductsByReference(reference);

            } catch (IllegalArgumentException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping("/search")
    public List<ProductViewModel> getProductsBySearch(@RequestParam("search") String name) {
        try {

            return productService.getProductsBySearch(name);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (ProductException e) {
            throw e;
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage(), e);
            throw new UnknownErrorException("Error: unknown error");
        }
    }

    @PutMapping
    public ProductViewModel update(@RequestHeader("Authorization") String jwtHeader,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("stock") int stock,
            @RequestParam("category") String category,
            @RequestParam("active") boolean active) {

        if (jwtService.verifyJwtHeader(jwtHeader)) {

            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                ProductUpdateModel model = new ProductUpdateModel(id, name, description, price,
                        stock, category, active);

                if (images.get(0).isEmpty()) {
                    images = null;
                }

                return productService.update(reference, model, images);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (ImageException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @PutMapping("/deactivate")
    public ProductViewModel deactivate(@RequestHeader("Authorization") String jwtHeader,
            @RequestParam("id") int id) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return productService.deactivate(reference, id);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (ImageException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }
}
