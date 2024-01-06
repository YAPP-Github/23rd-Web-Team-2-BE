package com.baro.common.infra.aws.s3;

import com.baro.common.image.ImageUploadException;
import com.baro.common.image.ImageUploader;
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
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
public class AwsImageUploader implements ImageUploader {

    private final S3Client s3Client;
    private final AwsS3Property awsS3Property;

    /**
     * p3. 파일의 접근 권한 관련도 있으면 좋을 것 같아요.
     * Download 로직을 어떻게 가져 가실지 잘 모르겠지만
     * 경우에 따라서는 특정 유저만 (업로더 본인이라던가) 파일에 접근하게 해야하는 경우가 생길 수 있다고 생각합니다.
     * 저는 AWS S3를 쓰는 경우에는 meta-data를 이용해서 권한 표기를 하는 편입니다. (double check로 DB에서도 관리하고 있어요)
     */
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

    private String changeToUrlSafetyKey(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String replacedFileName = Objects.requireNonNull(originalFilename).replace(" ", "_");
        return replacedFileName + "-" + UUID.randomUUID();
    }
}
