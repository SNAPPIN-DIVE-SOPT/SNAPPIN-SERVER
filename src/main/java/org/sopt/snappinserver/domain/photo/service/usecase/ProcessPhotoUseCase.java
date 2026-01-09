package org.sopt.snappinserver.domain.photo.service.usecase;

import org.sopt.snappinserver.domain.photo.service.dto.request.PhotoProcessCommand;

public interface ProcessPhotoUseCase {

    void linkPhotoWithMoodTags(PhotoProcessCommand photoProcessCommand);
}
