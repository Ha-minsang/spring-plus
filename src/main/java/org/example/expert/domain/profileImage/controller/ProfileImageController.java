package org.example.expert.domain.profileImage.controller;

import org.example.expert.domain.profileImage.service.ProfileImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @Autowired
    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = profileImageService.uploadProfileImage(image);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }
}
