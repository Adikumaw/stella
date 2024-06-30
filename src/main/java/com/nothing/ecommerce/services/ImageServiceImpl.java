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
import com.nothing.ecommerce.exception.UnableToUpdateProductDirectoryException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public List<String> save(int userId, String name, List<MultipartFile> images, String destinationPath) {
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
            }
        } catch (IOException e) {
            throw new ImageException("Error occurred while saving image: ", e);
        }

        return imageUrls;
    }

    @Override
    public String save(int userId, MultipartFile image, String destinationPath) {
        String imageUrl = null;

        try {

            // Validate filename extension
            String originalFilename = image.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            if (!isValidImageExtension(extension)) {
                throw new InvalidImageExtentionException("Error: Invalid Image Extention " + extension);
            }

            // Construct destination file path
            StringBuilder filePathBuilder = new StringBuilder();
            filePathBuilder.append(destinationPath).append("/")
                    .append(userId).append("/")
                    .append("logo").append(".").append(extension);
            String filePath = filePathBuilder.toString();

            File destinationFile = new File(filePath);
            File userDirectory = destinationFile.getParentFile();

            // Create parent directories if they don't exist
            if (!userDirectory.exists()) {
                userDirectory.mkdirs();
            }

            // Save the file
            FileUtils.copyInputStreamToFile(image.getInputStream(), destinationFile);

            // Add the file URL to the list
            imageUrl = filePath;
        } catch (IOException e) {
            throw new ImageException("Error occurred while saving image: ", e);
        }

        return imageUrl;
    }

    private boolean isValidImageExtension(String extension) {
        // Define valid image extensions
        List<String> validExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");
        return validExtensions.contains(extension.toLowerCase());
    }

    @Override
    public List<String> update(int userId, String oldName, String newName, List<MultipartFile> images,
            String destinationPath) {
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
                        .append(newName).append("/")
                        .append(i).append(".").append(extension);
                String filePath = filePathBuilder.toString();

                File destinationFile = new File(filePath);
                File productNameDirectory = destinationFile.getParentFile();

                // Create parent directories if they don't exist
                if (!productNameDirectory.exists()) {
                    productNameDirectory.mkdirs();
                }

                // Save the file
                FileUtils.copyInputStreamToFile(images.get(i).getInputStream(), destinationFile);

                // Add the file URL to the list
                imageUrls.add(filePath);
            }
        } catch (IOException e) {
            throw new ImageException("Error occurred while saving image: ", e);
        }
        if (oldName != newName) {
            // Construct old Directory path
            StringBuilder directoryPathBuilder = new StringBuilder();
            directoryPathBuilder.append(destinationPath).append("/")
                    .append(userId).append("/")
                    .append(oldName);
            String directoryPath = directoryPathBuilder.toString();
            File directory = new File(directoryPath);

            // Delete old directory
            deleteDirectory(directory);
        } else {
            // Construct old Directory path
            StringBuilder directoryPathBuilder = new StringBuilder();
            directoryPathBuilder.append(destinationPath).append("/")
                    .append(userId).append("/")
                    .append(oldName);
            String directoryPath = directoryPathBuilder.toString();
            File directory = new File(directoryPath);

            // Delete old additional images if they exist
            File[] oldImages = directory.listFiles();
            for (int i = images.size(); i < oldImages.length; i++) {
                oldImages[i].delete();
            }
        }

        return imageUrls;
    }

    @Override
    public List<String> renameDirectory(int userId, String oldName, String newName, String destinationPath) {
        // Construct old Directory path
        StringBuilder oldDirectoryPathBuilder = new StringBuilder();
        oldDirectoryPathBuilder.append(destinationPath).append("/")
                .append(userId).append("/")
                .append(oldName);
        String oldDirectoryPath = oldDirectoryPathBuilder.toString();

        // Construct new Directory path
        StringBuilder newDirectoryPathBuilder = new StringBuilder();
        newDirectoryPathBuilder.append(destinationPath).append("/")
                .append(userId).append("/")
                .append(newName);
        String newDirectoryPath = newDirectoryPathBuilder.toString();

        File oldDirectory = new File(oldDirectoryPath);
        File newDirectory = new File(newDirectoryPath);

        // rename the old directory
        if (oldDirectory.renameTo(newDirectory)) {
            // fetch new image urls
            List<String> imageUrls = new ArrayList<String>();
            for (File file : newDirectory.listFiles()) {
                imageUrls.add(file.getAbsolutePath());
            }
            return imageUrls;
        } else {
            throw new UnableToUpdateProductDirectoryException("Error: Unable to update product directory");
        }
    }

    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    deleteDirectory(file); // Recursively delete subdirectories
                } else {
                    file.delete(); // Delete files
                }
            }
            // After deleting all contents, delete the directory itself
            directory.delete();
        }
    }
}
