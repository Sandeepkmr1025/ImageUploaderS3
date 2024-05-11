package com.sandeep.imageuploaders3.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageUploader {

    String uploadImage(MultipartFile image);

    List<String> getAllImages();

    String preSignedURL(String filename);

    String getImageURLByName(String filename);

}
