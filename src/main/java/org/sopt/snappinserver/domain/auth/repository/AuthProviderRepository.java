package org.sopt.snappinserver.domain.auth.repository;

import org.sopt.snappinserver.domain.auth.domain.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

}
