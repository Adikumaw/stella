package com.nothing.ecommerce.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.entity.Product;
import com.nothing.ecommerce.model.ProductIdAndNameModel;
import com.nothing.ecommerce.model.ProductInputModel;
import com.nothing.ecommerce.model.ProductUpdateModel;
import com.nothing.ecommerce.model.ProductViewModel;

public interface ProductService {
    List<ProductViewModel> getProductsBySearch(String search);

    List<ProductViewModel> getProductsByReference(String reference);

    List<ProductViewModel> getProductsByStoreName(String storeName);

    ProductViewModel save(String reference, ProductInputModel product, List<MultipartFile> images);

    ProductViewModel update(String reference, ProductUpdateModel model, List<MultipartFile> images);

    ProductViewModel deactivate(String reference, int productId);

    ProductViewModel activate(String reference, int productId);

    int findIdByCategory(String category);

    Double findPriceById(int productId);

    List<ProductIdAndNameModel> findProductIdAndNameByUserId(int userId);

    int findUserIdByProductId(int productId);

    Product findById(int productId);

    Boolean existsById(int productId);

}
