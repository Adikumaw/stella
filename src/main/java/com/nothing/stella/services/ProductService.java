package com.nothing.stella.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nothing.stella.entity.Product;
import com.nothing.stella.model.ProductIdAndNameModel;
import com.nothing.stella.model.ProductInputModel;
import com.nothing.stella.model.ProductUpdateModel;
import com.nothing.stella.model.ProductViewModel;

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
