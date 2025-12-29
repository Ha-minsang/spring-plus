package org.example.expert.domain.profileImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProfileImageService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public ProfileImageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public String uploadProfileImage(MultipartFile file) throws IOException {

        String fileName = generateFileName(file.getOriginalFilename());

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }
}

