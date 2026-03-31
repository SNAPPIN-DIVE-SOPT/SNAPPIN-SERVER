package org.sopt.snappinserver.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "onboarding_snap_category")
public class OnboardingSnapCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "onboarding_snap_category_seq_gen")
    @SequenceGenerator(
        name = "onboarding_snap_category_seq_gen",
        sequenceName = "onboarding_snap_category_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Onboarding onboarding;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnapCategory snapCategory;

    @Builder(access = AccessLevel.PRIVATE)
    private OnboardingSnapCategory(Onboarding onboarding, SnapCategory snapCategory) {
        this.onboarding = onboarding;
        this.snapCategory = snapCategory;
    }

    public static OnboardingSnapCategory create(Onboarding onboarding, SnapCategory snapCategory) {
        return OnboardingSnapCategory.builder()
            .onboarding(onboarding)
            .snapCategory(snapCategory)
            .build();
    }
}
