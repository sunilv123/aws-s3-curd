package com.s3.service;

public interface S3Service {

    String generatePreSignedUrl(String fileName) throws Exception;

    String downloadPreSignedUrl(String objectKey);

    void deleteFile(String objectKey);

}
