package com.nothing.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.client.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQueryBuilder;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.entity.Product;
import com.nothing.ecommerce.entity.ProductCategory;
import com.nothing.ecommerce.exception.EmptyImagesException;
import com.nothing.ecommerce.exception.ImageException;
import com.nothing.ecommerce.exception.InvalidProductCategoryException;
import com.nothing.ecommerce.exception.InvalidProductException;
import com.nothing.ecommerce.exception.UsedProductNameException;
import com.nothing.ecommerce.model.ProductInputModel;
import com.nothing.ecommerce.model.ProductViewModel;
import com.nothing.ecommerce.repository.ProductCategoryRepository;
import com.nothing.ecommerce.repository.ProductESRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductESRepository productESRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductCategoryRepository categoryRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    private final String path = "/home/all_father/Documents/workshop/java/ecommerce/src/main/resources/static/img";

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductViewModel save(String reference, ProductInputModel model, List<MultipartFile> images) {
        // verify product input model
        if (!verify(model)) {
            throw new InvalidProductException("Error: Invalid product");
        }
        // verify Images input is not empty
        if (images == null || images.isEmpty()) {
            throw new EmptyImagesException("Error: Empty images");
        }

        int userId = userService.findUserIdByReference(reference);

        // verify product name is not used before
        if (productESRepository.findByUserIdAndName(userId, model.getName()) != null) {
            throw new UsedProductNameException("Error: product name is already used");
        }

        List<String> imageUrls = imageService.saveImages(userId, model.getName(), images, path);

        if (imageUrls.isEmpty()) {
            throw new ImageException("Error: Failed to save images");
        }

        Product product = productBuilder(userId, model, imageUrls);

        try {
            productESRepository.save(product);
        } catch (Exception e) {
            logger.warn("Exception while saving product: " + e.getMessage());
        }

        String name = product.getName();
        product = productESRepository.findByUserIdAndName(userId, name);
        while (product == null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.warn("Sleep Interupted");
            }
            product = productESRepository.findByUserIdAndName(userId, name);
        }

        return convertToProductViewModel(product);
    }

    @Override
    public List<ProductViewModel> getProductsBySearch(String search) {

        List<Product> ESProducts = productESRepository.findByName(search);
        if (ESProducts != null) {
            List<ProductViewModel> productsViewModels = new ArrayList<ProductViewModel>();
            for (Product product : ESProducts) {
                productsViewModels.add(convertToProductViewModel(product));
            }
            return productsViewModels;
        } else {
            return null;
        }
    }

    @Override
    public List<ProductViewModel> getProductsByReference(String reference) {
        int userId = userService.findUserIdByReference(reference);

        List<Product> products = productESRepository.findByUserId(userId);
        if (products != null) {
            List<ProductViewModel> productsViewModels = new ArrayList<ProductViewModel>();
            for (Product product : products) {
                productsViewModels.add(convertToProductViewModel(product));
            }
            return productsViewModels;
        } else {
            return null;
        }
    }

    private ProductViewModel convertToProductViewModel(Product product) {
        Optional<ProductCategory> productCategory = categoryRepository.findById(product.getCategoryId());
        if (productCategory.isPresent()) {
            return new ProductViewModel(product.getId(), product.getName(), product.getDescription(),
                    product.getPrice(),
                    product.getStock(), productCategory.get().getCategory(), product.getImage1(), product.getImage2(),
                    product.getImage3(),
                    product.getImage4(), product.getImage5(), product.getImage6(), product.getImage7(),
                    product.getImage8(), product.getImage9());
        } else {
            throw new InvalidProductCategoryException("Error: Invalid product category");
        }
    }

    @Override
    public ProductInputModel update(ProductInputModel product, MultipartFile images) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public Product productBuilder(int userId, ProductInputModel model, List<String> imageUrls) {
        int imageUrlsSize = imageUrls.size();
        for (int i = imageUrlsSize; i < 9; i++) {
            imageUrls.add(null);
        }
        int categoryId = categoryRepository.findIdByCategory(model.getCategory());

        Product product = Product.builder()
                .userId(userId)
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .stock(model.getStock())
                .categoryId(categoryId)
                .image1(imageUrls.get(0))
                .image2(imageUrls.get(1))
                .image3(imageUrls.get(2))
                .image4(imageUrls.get(3))
                .image5(imageUrls.get(4))
                .image6(imageUrls.get(5))
                .image7(imageUrls.get(6))
                .image8(imageUrls.get(7))
                .image9(imageUrls.get(8))
                .build();

        System.out.println(product.toString());

        return product;
    }

    @Override
    public Boolean verify(ProductInputModel product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            return false;
        } else if (product.getDescription() == null || product.getDescription().isEmpty()) {
            return false;
        } else if (product.getPrice() < 1) {
            return false;
        } else if (product.getStock() <= 1) {
            return false;
        } else if (product.getCategory() == null || product.getCategory().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
