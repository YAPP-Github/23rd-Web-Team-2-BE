package com.baro.common.infra.aws.s3;

import com.baro.common.client.ImageUploader;
import org.springframework.web.multipart.MultipartFile;

public class AwsImageUploader implements ImageUploader {

    @Override
    public void upload(final MultipartFile file) {
        
    }
}
