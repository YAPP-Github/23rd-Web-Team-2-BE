package com.baro.common.image;

import com.baro.common.image.dto.ImageUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    ImageUploadResult upload(MultipartFile file);
}
