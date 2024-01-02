package com.baro.common.infra.aws.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record AwsS3Property(
        String bucketName,
        String key,
        String accessKey,
        String secretKey
) {
}
