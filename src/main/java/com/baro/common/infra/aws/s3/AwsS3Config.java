package com.baro.common.infra.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@RequiredArgsConstructor
@Configuration
public class AwsS3Config {

    private final AwsS3Property awsS3Property;

    @Bean
    public S3Client amazonS3Client() {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(awsS3Property.accessKey(), awsS3Property.secretKey());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}
