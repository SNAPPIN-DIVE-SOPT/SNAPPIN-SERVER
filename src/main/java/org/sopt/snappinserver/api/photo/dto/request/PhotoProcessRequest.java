package org.sopt.snappinserver.api.photo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "무드 태그와 연결할 사진 정보 DTO")
public record PhotoProcessRequest(

    @Schema(name = "사진의 S3 key 값")
    String imageUrl,

    @Schema(name = "사진의 벡터 변환 값")
    List<Float> embedding
) {

}
