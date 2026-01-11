package org.sopt.snappinserver.domain.user.service.dto.response;

import java.util.List;

public record GetClientInfoResult(
    String name,
    List<String> curatedMoods
) {

}
