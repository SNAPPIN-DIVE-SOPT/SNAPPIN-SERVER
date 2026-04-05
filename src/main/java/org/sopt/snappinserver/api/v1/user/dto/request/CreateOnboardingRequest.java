package org.sopt.snappinserver.api.v1.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.sopt.snappinserver.global.enums.Gender;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Schema(description = "온보딩 정보 입력 요청 DTO")
public record CreateOnboardingRequest(

    @Schema(description = "이름")
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 4, message = "이름은 최대 4글자입니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    String name,

    @Schema(description = "성별입니다. MALE, FEMALE 중 하나로 요청해 주세요.")
    @NotNull(message = "성별은 필수입니다.")
    Gender gender,

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 한글, 영문, 숫자 2~10자만 입력 가능합니다.")
    String nickname,

    @Schema(description = "전화번호입니다. 010-0000-0000 형태로 요청해주세요.")
    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    String phoneNumber,

    @Schema(description = "이메일")
    @NotBlank(message = "이메일은 필수입니다.")
    @Size(max = 320, message = "이메일은 최대 320자입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @Schema(description = "원하는 스냅 카테고리 목록")
    @NotEmpty(message = "스냅 카테고리는 필수입니다.")
    List<SnapCategory> snapCategories
) {

}
