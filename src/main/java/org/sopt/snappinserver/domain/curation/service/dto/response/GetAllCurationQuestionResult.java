package org.sopt.snappinserver.domain.curation.service.dto.response;

import java.util.List;

public record GetAllCurationQuestionResult(
    List<GetCurationQuestionResult> questions
) {

    public static GetAllCurationQuestionResult of(
        List<GetCurationQuestionResult> questions
    ) {
        return new GetAllCurationQuestionResult(
            questions == null ? List.of() : List.copyOf(questions)
        );
    }
}
