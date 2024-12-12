package com.s3.service.impl;

import com.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HexFormat;

@Service
public class S3ServiceImpl implements S3Service {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    S3Presigner presigner;

    @Autowired
    S3Client s3Client;

    @Override
    public String generatePreSignedUrl(String fileName) throws Exception {

        String objectKey = generateRandomKey(15) + "_" + fileName;

        // Generate presigned URL for upload (PUT request)
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PutObjectPresignRequest putPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(2))
                .putObjectRequest(putObjectRequest)
                .build();

        URL uploadUrl = presigner.presignPutObject(putPresignRequest).url();
        System.out.println("Presigned URL for upload: " + uploadUrl);

        presigner.close();
        return uploadUrl.toString();
    }

    @Override
    public String downloadPreSignedUrl(String objectKey) {

        // Generate presigned URL for download (GET request)
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest getPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(2))
                .getObjectRequest(getObjectRequest)
                .build();

        URL downloadUrl = presigner.presignGetObject(getPresignRequest).url();
        System.out.println("Presigned URL for download: " + downloadUrl);

        // Always close the presigner when done
        presigner.close();

        return downloadUrl.toString();
    }

    @Override
    public void deleteFile(String objectKey) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.deleteObject(deleteRequest);
        System.out.println("Deleted file: " + objectKey);
    }

    public String generateRandomKey(int length) {
        // Ensure length is positive
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer.");
        }

        // Create a secure random instance
        SecureRandom secureRandom = new SecureRandom();

        // Generate a byte array with sufficient randomness
        byte[] randomBytes = new byte[(int) Math.ceil(length / 2.0)];
        secureRandom.nextBytes(randomBytes);

        // Convert bytes to a hex string
        String randomHex = HexFormat.of().formatHex(randomBytes);

        // Truncate or return the required length
        return randomHex.length() > length ? randomHex.substring(0, length) : randomHex;
    }
}

