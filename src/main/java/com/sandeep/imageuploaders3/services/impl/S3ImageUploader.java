package com.sandeep.imageuploaders3.services.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.sandeep.imageuploaders3.exceptions.ImageUploadException;
import com.sandeep.imageuploaders3.services.ImageUploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3ImageUploader implements ImageUploader {

    private AmazonS3 s3Client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    S3ImageUploader(AmazonS3 s3Client, AmazonS3 client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadImage(MultipartFile image) {

        if (image==null) {
            throw new ImageUploadException("Image cannot be null");
        }
        String actualFilename = image.getOriginalFilename();

        assert actualFilename != null;
        String filename = UUID.randomUUID() + actualFilename.substring(actualFilename.lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());
        // Uploading the image in S3
        try {
            PutObjectResult putObjectResult = s3Client.putObject(new PutObjectRequest(bucketName, filename, image.getInputStream(), metadata));
            return this.preSignedURL(filename);
        } catch (IOException e) {
            throw new ImageUploadException("Error in uploading the image" + e.getMessage());
        }
    }

    @Override
    public List<String> getAllImages() {

        ObjectListing objectListing = s3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries().stream().map(item -> this.preSignedURL(item.getKey())).toList();
    }

    @Override
    public String preSignedURL(String filename) {

        int hour = 1;
        Date expiration = new Date(System.currentTimeMillis() + hour * 3600 * 1000);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, filename)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Override
    public String getImageURLByName(String filename) {
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        return this.preSignedURL(s3Object.getKey());
    }
}
