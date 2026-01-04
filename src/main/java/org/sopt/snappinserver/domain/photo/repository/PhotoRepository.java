package org.sopt.snappinserver.domain.photo.repository;

import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
