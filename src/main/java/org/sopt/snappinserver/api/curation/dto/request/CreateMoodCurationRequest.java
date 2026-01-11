package org.sopt.snappinserver.api.curation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "무드 큐레이션 결과 저장 요청 DTO")
public record CreateMoodCurationRequest(

    @Schema(description = "사용자가 선택한 사진 ID 목록", example = "[1, 2, 3, 4, 5]")
    @NotNull(message = "사진 ID 목록은 비어있을 수 없습니다.")
    @Size(min = 5, message = "사용자가 선택한 사진 개수는 5개여야 합니다.")
    @Size(max = 5, message = "사용자가 선택한 사진 개수는 5개여야 합니다.")
    List<Long> photoIds
) {

}
