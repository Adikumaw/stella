package com.nothing.ecommerce.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    List<String> save(int userId, String name, List<MultipartFile> images, String destinationPath);

    String save(int userId, MultipartFile image, String destinationPath);

    List<String> update(int userId, String oldName, String newName, List<MultipartFile> images, String destinationPath);

    List<String> renameDirectory(int userId, String oldName, String newName, String destinationPath);
}
