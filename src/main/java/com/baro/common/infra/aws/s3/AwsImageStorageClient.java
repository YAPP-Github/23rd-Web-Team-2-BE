package com.baro.common.infra.aws.s3;

import com.baro.common.image.ImageStorageClient;
import com.baro.common.image.ImageUploadException;
import com.baro.common.image.dto.ImageUploadResult;
import com.baro.common.utils.ImageExtensionConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
public class AwsImageStorageClient implements ImageStorageClient {

    private final S3Client s3Client;
    private final AwsS3Property awsS3Property;

    @Override
    public ImageUploadResult upload(MultipartFile file) {
        String urlSafetyKey = changeToUrlSafetyKey(file);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsS3Property.bucket())
                .key(awsS3Property.key() + urlSafetyKey)
                .contentType(file.getContentType())
                .contentDisposition("inline")
                .build();

        try {
            byte[] jpegImageBytes = ImageExtensionConverter.toJpeg(file);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jpegImageBytes);
            RequestBody requestBody = RequestBody.fromInputStream(inputStream, jpegImageBytes.length);
            inputStream.close();
            s3Client.putObject(putObjectRequest, requestBody);
            return new ImageUploadResult(urlSafetyKey);
        } catch (IOException ioException) {
            throw new ImageUploadException("Error on converting or uploading image", ioException);
        }
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(awsS3Property.bucket())
                .key(awsS3Property.key() + key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String changeToUrlSafetyKey(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String replacedFileName = Objects.requireNonNull(originalFilename).replace(" ", "_");
        return replacedFileName + "-" + UUID.randomUUID();
    }
}
