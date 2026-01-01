package org.sopt.snappinserver.domain.user.repository;

import org.sopt.snappinserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
