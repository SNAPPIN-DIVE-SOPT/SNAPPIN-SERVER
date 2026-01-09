package org.sopt.snappinserver.domain.mood.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.sopt.snappinserver.domain.mood.repository.MoodWithScore;
import org.springframework.stereotype.Component;

@Component
public class MoodSelector {

    private static final List<Set<String>> OPPOSITE_PAIRS = List.of(
        Set.of("내추럴", "연출된"),
        Set.of("아날로그", "디지털")
    );

    public List<MoodWithScore> selectTop3(int lastRank, List<MoodWithScore> candidates) {
        List<MoodWithScore> selected = new ArrayList<>();

        for (MoodWithScore candidate : candidates) {
            if (selected.size() >= lastRank) {
                break;
            }
            if (hasConflict(candidate, selected)) {
                continue;
            }
            selected.add(candidate);
        }
        return selected;
    }

    private boolean hasConflict(MoodWithScore candidate, List<MoodWithScore> selected) {
        return selected.stream()
            .anyMatch(s -> isOpposite(candidate.name(), s.name()));
    }

    private boolean isOpposite(String firstTag, String secondTag) {
        return OPPOSITE_PAIRS.stream()
            .anyMatch(pair -> pair.contains(firstTag) && pair.contains(secondTag));
    }
}
