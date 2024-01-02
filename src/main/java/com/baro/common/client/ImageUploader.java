package com.baro.common.client;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    void upload(MultipartFile file);
}
