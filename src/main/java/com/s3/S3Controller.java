package com.s3;

import com.s3.dto.FileDto;
import com.s3.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class S3Controller {

    @Autowired
    private S3ServiceImpl s3ServiceImpl;

    @PostMapping("/s3/upload-url")
    public String getUploadUrl(@RequestBody FileDto dto) throws Exception {
        return s3ServiceImpl.generatePreSignedUrl(dto.getFileName());
    }

    @GetMapping("/s3/download-url")
    public String getDownloadUrl(@RequestParam String key) {
        return s3ServiceImpl.downloadPreSignedUrl(key);
    }

    @DeleteMapping("/s3/delete")
    public String deleteFile(@RequestParam String objectKey) {
        s3ServiceImpl.deleteFile(objectKey);
        return "File deleted successfully: " + objectKey;
    }

}
