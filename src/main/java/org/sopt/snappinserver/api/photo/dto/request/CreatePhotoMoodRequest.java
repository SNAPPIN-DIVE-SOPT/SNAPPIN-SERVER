package org.sopt.snappinserver.api.photo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;

@Schema(name = "무드 태그와 연결할 사진 정보 DTO")
public record CreatePhotoMoodRequest(

    @Schema(name = "사진의 S3 key 값")
    @NotBlank @Length(max = 1024) String imageUrl,

    @Schema(name = "사진의 벡터 변환 값")
    @NotNull List<Float> embedding
) {

}
