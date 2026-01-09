package org.sopt.snappinserver.domain.photographer.repository;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographerScheduleRepository extends JpaRepository<PhotographerSchedule, Long> {

    List<PhotographerSchedule> findAllByPhotographerAndDayOffTrue(
        Photographer photographer
    );
}
