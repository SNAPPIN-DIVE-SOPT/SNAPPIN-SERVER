package org.sopt.snappinserver.domain.user.repository;

import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {

    boolean existsByUser(User user);
}
