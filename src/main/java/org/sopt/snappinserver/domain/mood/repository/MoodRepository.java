package org.sopt.snappinserver.domain.mood.repository;

import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodRepository extends JpaRepository<Mood, Long> {

}
