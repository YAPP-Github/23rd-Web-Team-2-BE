package com.baro.common.image;

import com.baro.common.image.dto.ImageUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageClient {

    ImageUploadResult upload(MultipartFile file);

    void delete(String key);
}
