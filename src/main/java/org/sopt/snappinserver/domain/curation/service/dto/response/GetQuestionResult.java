package org.sopt.snappinserver.domain.curation.service.dto.response;

import org.sopt.snappinserver.domain.question.domain.entity.Question;

public record GetQuestionResult(Long id, String contents, Integer step) {

    public static GetQuestionResult from(Question question) {
        return new GetQuestionResult(question.getId(), question.getContents(), question.getStep());
    }
}
