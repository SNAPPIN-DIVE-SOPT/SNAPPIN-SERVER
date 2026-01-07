package org.sopt.snappinserver.domain.auth.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.auth.domain.entity.AuthProvider;
import org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

    Optional<AuthProvider> findBySocialProviderAndProviderId(
        SocialProvider socialProvider,
        String providerId
    );
}
