package org.sopt.snappinserver.api.photo.dto.request;

import java.util.List;

public record PhotoProcessRequest(String imageUrl, List<Float> embedding) {

}
