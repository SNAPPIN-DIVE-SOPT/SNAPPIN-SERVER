package org.sopt.snappinserver.domain.curation.service.dto.response;

import java.util.List;
import java.util.Objects;
import org.sopt.snappinserver.domain.question.domain.entity.Question;

public record GetCurationQuestionResult(
    GetQuestionResult question,
    List<GetPhotoResult> photos
) {

    public static GetCurationQuestionResult of(
        Question question,
        List<GetPhotoResult> photos
    ) {
        Objects.requireNonNull(question, "질문은 null일 수 없습니다.");
        return new GetCurationQuestionResult(
            GetQuestionResult.from(question),
            photos == null ? List.of(): List.copyOf(photos)
        );
    }
}
