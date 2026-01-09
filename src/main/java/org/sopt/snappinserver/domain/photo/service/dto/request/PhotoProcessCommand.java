package org.sopt.snappinserver.domain.photo.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.api.photo.dto.request.PhotoProcessRequest;

public record PhotoProcessCommand(String imageUrl, List<Float> embedding) {

    public static PhotoProcessCommand create(PhotoProcessRequest photoProcessRequest) {
        return new PhotoProcessCommand(
            photoProcessRequest.imageUrl(),
            photoProcessRequest.embedding()
        );
    }

}
