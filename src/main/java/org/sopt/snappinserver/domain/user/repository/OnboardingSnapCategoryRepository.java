package org.sopt.snappinserver.domain.user.repository;

import org.sopt.snappinserver.domain.user.domain.entity.OnboardingSnapCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingSnapCategoryRepository
    extends JpaRepository<OnboardingSnapCategory, Long> {

}
