package org.sopt.snappinserver.domain.review.service.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResult {
    private Long id;
    private String reviewer;
    private int rating;
    private LocalDate createdAt;
    private List<String> images;
    private String content;
}
