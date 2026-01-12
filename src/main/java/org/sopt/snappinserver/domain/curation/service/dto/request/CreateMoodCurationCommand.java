package org.sopt.snappinserver.domain.curation.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.api.curation.dto.request.CreateMoodCurationRequest;

public record CreateMoodCurationCommand(Long userId, List<Long> photoIds) {

    public static CreateMoodCurationCommand of(
        Long userId,
        CreateMoodCurationRequest createMoodCurationRequest
    ) {
        return new CreateMoodCurationCommand(userId, createMoodCurationRequest.photoIds());
    }
}
