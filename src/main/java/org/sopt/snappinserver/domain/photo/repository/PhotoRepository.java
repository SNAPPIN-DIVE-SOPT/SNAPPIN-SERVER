package org.sopt.snappinserver.domain.photo.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.global.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByImageUrl(String imageUrl);

    List<Photo> findAllByGender(Gender gender);
}
