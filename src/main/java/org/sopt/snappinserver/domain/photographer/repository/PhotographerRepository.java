package org.sopt.snappinserver.domain.photographer.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    Optional<Photographer> findByUser(User user);

}
