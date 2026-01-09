package org.sopt.snappinserver.domain.auth.service;

import static org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider.KAKAO;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.entity.AuthProvider;
import org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider;
import org.sopt.snappinserver.domain.auth.repository.AuthProviderRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.enums.UserRole;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class GetSocialUserService {

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;

    @Transactional
    public User registerOrGetUser(
        SocialProvider socialProvider,
        String providerId,
        String name,
        String profileImage
    ) {
        return authProviderRepository.findBySocialProviderAndProviderId(socialProvider, providerId)
            .map(AuthProvider::getUser)
            .orElseGet(() -> createUser(providerId, name, profileImage));
    }

    private User createUser(String providerId, String name, String profileImage) {
        User user = userRepository.save(User.create(UserRole.CLIENT, name, profileImage));
        authProviderRepository.save(AuthProvider.create(user, KAKAO, providerId));

        return user;
    }
}
