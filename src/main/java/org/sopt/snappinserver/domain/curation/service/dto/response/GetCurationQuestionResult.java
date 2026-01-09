package org.sopt.snappinserver.domain.curation.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.question.domain.entity.Question;

public record GetCurationQuestionResult(
    GetQuestionResult question,
    List<GetPhotoResult> photos
) {

    public static GetCurationQuestionResult of(
        Question question,
        List<GetPhotoResult> photos
    ) {
        return new GetCurationQuestionResult(GetQuestionResult.from(question), photos);
    }
}
