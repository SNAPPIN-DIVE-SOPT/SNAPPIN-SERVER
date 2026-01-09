package org.sopt.snappinserver.domain.mood.repository;

import java.util.List;

public interface MoodVectorRepository {

    List<MoodWithScore> findTopCandidates(String embedding);
}
