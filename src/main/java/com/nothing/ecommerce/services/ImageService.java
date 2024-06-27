package com.nothing.ecommerce.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    List<String> saveImages(int userId, String name, List<MultipartFile> images, String destinationPath);
}
