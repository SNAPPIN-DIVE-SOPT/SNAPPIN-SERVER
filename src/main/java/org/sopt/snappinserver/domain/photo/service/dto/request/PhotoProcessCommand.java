package org.sopt.snappinserver.domain.photo.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.api.photo.dto.request.CreatePhotoMoodRequest;

public record PhotoProcessCommand(String imageUrl, List<Float> embedding) {

    public static PhotoProcessCommand from(CreatePhotoMoodRequest createPhotoMoodRequest) {
        return new PhotoProcessCommand(
            createPhotoMoodRequest.imageUrl(),
            createPhotoMoodRequest.embedding()
        );
    }

}
