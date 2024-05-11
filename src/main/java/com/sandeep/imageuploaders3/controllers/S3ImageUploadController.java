package com.sandeep.imageuploaders3.controllers;

import com.sandeep.imageuploaders3.services.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3")
public class S3ImageUploadController {

    private ImageUploader uploader;

    public S3ImageUploadController(ImageUploader imageUploader) {
        this.uploader = imageUploader;
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(uploader.uploadImage(file));
    }

    @GetMapping
    public ResponseEntity<?> getAllImages()
    {
        return ResponseEntity.ok(uploader.getAllImages());
    }

    @GetMapping("/{filename}")
    public String getImageURLByName(@PathVariable("filename") String filename)
    {
        return uploader.getImageURLByName(filename);
    }


}
