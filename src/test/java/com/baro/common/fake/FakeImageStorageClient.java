package com.baro.common.fake;

import com.baro.common.image.ImageStorageClient;
import com.baro.common.image.dto.ImageUploadResult;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.multipart.MultipartFile;

public class FakeImageStorageClient implements ImageStorageClient {

    private final Map<String, MultipartFile> storage = new ConcurrentHashMap<>();

    @Override
    public ImageUploadResult upload(MultipartFile file) {
        storage.put(file.getOriginalFilename(), file);
        return new ImageUploadResult(file.getOriginalFilename());
    }

    @Override
    public void delete(String key) {
        storage.remove(key);
    }
}
