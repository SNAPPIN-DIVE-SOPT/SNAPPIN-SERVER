package org.sopt.snappinserver.global.s3;

import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final int DURATION_MINUTES = 10;

    private final S3Presigner s3Presigner;

    @NotBlank
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String getPresignedUrl(String storedFileName) {
        try {
            validateStoredFileNameExsits(storedFileName);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storedFileName)
                .build();

            PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(p -> p
                    .signatureDuration(Duration.ofMinutes(DURATION_MINUTES))
                    .getObjectRequest(getObjectRequest)
                );

            return presignedRequest.url().toString();
        } catch (Exception e) {
            throw new S3Exception(S3ErrorCode.S3_SERVICE_UNAVAILABLE);
        }
    }

    private static void validateStoredFileNameExsits(String storedFileName) {
        if(storedFileName == null || storedFileName.isBlank()) {
            throw new S3Exception(S3ErrorCode.S3_KEY_REQUIRED);
        }
    }
}
