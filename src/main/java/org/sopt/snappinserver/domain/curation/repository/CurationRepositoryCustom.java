package org.sopt.snappinserver.domain.curation.repository;

import java.util.List;

public interface CurationRepositoryCustom {

    List<Long> findTop3MoodIdsByUserId(Long userId);
}
