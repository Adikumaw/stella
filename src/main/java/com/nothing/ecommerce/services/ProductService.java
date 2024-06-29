package com.nothing.ecommerce.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.model.ProductInputModel;
import com.nothing.ecommerce.model.ProductUpdateModel;
import com.nothing.ecommerce.model.ProductViewModel;

public interface ProductService {
    List<ProductViewModel> getProductsBySearch(String search);

    List<ProductViewModel> getProductsByReference(String reference);

    ProductViewModel save(String reference, ProductInputModel product, List<MultipartFile> images);

    ProductViewModel update(String reference, ProductUpdateModel model, List<MultipartFile> images);

    ProductViewModel deactivate(String reference, int productId);
}
