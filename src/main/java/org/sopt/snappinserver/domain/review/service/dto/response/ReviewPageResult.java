package org.sopt.snappinserver.domain.review.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewPageResult {
    private List<ReviewResult> reviews;
    private Long nextCursor;
    private boolean hasNext;
}