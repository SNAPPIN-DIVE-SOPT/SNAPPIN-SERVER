package org.sopt.snappinserver.global.config.s3;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Profile("!test")
@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    private StaticCredentialsProvider credentialsProvider() {
        AwsBasicCredentials credentials =
            AwsBasicCredentials.create(accessKey, secretKey);
        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider())
            .build();
    }
}
