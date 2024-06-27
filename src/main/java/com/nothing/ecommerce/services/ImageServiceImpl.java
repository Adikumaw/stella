package com.nothing.ecommerce.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.exception.ImageException;
import com.nothing.ecommerce.exception.InvalidImageExtentionException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public List<String> saveImages(int userId, String name, List<MultipartFile> images, String destinationPath) {
        List<String> imageUrls = new ArrayList<>();

        try {
            for (int i = 0; i < images.size(); i++) {
                // Validate filename extension
                String originalFilename = images.get(i).getOriginalFilename();
                String extension = FilenameUtils.getExtension(originalFilename);
                if (!isValidImageExtension(extension)) {
                    throw new InvalidImageExtentionException("Error: Invalid Image Extention " + extension);
                }

                // Construct destination file path
                StringBuilder filePathBuilder = new StringBuilder();
                filePathBuilder.append(destinationPath).append("/")
                        .append(userId).append("/")
                        .append(name).append("/")
                        .append(i).append(".").append(extension);
                String filePath = filePathBuilder.toString();

                File destinationFile = new File(filePath);
                File productNameDirectory = destinationFile.getParentFile();
                File userDirectory = productNameDirectory.getParentFile();

                // Create parent directories if they don't exist
                if (!userDirectory.exists()) {
                    userDirectory.mkdirs();
                }
                if (!productNameDirectory.exists()) {
                    productNameDirectory.mkdirs();
                }

                // Save the file
                FileUtils.copyInputStreamToFile(images.get(i).getInputStream(), destinationFile);

                // Add the file URL to the list
                imageUrls.add(filePath);
                System.out.println(filePath);
            }
        } catch (IOException e) {
            throw new ImageException("Error occurred while saving image: ", e);
        }

        return imageUrls;
    }

    private boolean isValidImageExtension(String extension) {
        // Define valid image extensions
        List<String> validExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");
        return validExtensions.contains(extension.toLowerCase());
    }
}
