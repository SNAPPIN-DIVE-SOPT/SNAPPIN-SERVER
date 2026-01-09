package org.sopt.snappinserver.domain.photo.repository;

import org.sopt.snappinserver.domain.photo.domain.entity.PhotoMood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoMoodRepository extends JpaRepository<PhotoMood, Long> {

}
