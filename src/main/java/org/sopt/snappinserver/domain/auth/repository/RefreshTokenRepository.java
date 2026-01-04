package org.sopt.snappinserver.domain.auth.repository;

import org.sopt.snappinserver.domain.auth.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
