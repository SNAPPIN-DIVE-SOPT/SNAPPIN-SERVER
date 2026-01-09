package org.sopt.snappinserver.api.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetPhotoResult;

@Schema(description = "질문과 연관된 사진 응답 DTO")
public record GetPhotoResponse(

    @Schema(description = "사진 ID", example = "1")
    Long id,

    @Schema(description = "사진 presigned url", example = "https://abc.com")
    String imageUrl,

    @Schema(description = "사진 순서", example = "1")
    Integer order
) {

    public static GetPhotoResponse from(GetPhotoResult getPhotoResult) {
        return new GetPhotoResponse(
            getPhotoResult.id(),
            getPhotoResult.imageUrl(),
            getPhotoResult.order()
        );
    }
}
