package org.sopt.snappinserver.domain.mood.repository;

public record MoodWithScoreImpl(
    Long id,
    String name,
    Float score
) implements MoodWithScore {

}
