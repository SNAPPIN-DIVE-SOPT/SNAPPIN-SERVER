package org.sopt.snappinserver.api.v1.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.global.enums.Gender;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Schema(description = "온보딩 정보 입력 요청 DTO")
public record CreateOnboardingRequest(

    @Schema(description = "이름")
    String name,

    @Schema(description = "성별입니다. MALE, FEMALE 중 하나로 요청해 주세요.")
    Gender gender,

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "전화번호입니다. 010-0000-0000 형태로 요청해주세요.")
    String phoneNumber,

    @Schema(description = "이메일")
    String email,

    @Schema(description = "원하는 스냅 카테고리 목록")
    List<SnapCategory> snapCategories
) {

}
