package org.sopt.snappinserver.domain.curation.service.usecase;

import org.sopt.snappinserver.domain.curation.service.dto.request.CreateMoodCurationCommand;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;

public interface CreateMoodCurationUseCase {

    CreateMoodCurationResult saveMoodCurationResult(CreateMoodCurationCommand createMoodCurationCommand);
}
