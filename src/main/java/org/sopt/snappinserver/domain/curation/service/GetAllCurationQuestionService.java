package org.sopt.snappinserver.domain.curation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetAllCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetPhotoResult;
import org.sopt.snappinserver.domain.curation.service.usecase.GetAllCurationQuestionUseCase;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photo.repository.PhotoRepository;
import org.sopt.snappinserver.domain.question.domain.entity.Question;
import org.sopt.snappinserver.domain.question.domain.enums.QuestionDomain;
import org.sopt.snappinserver.domain.question.repository.QuestionRepository;
import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.OnboardingRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.global.enums.Gender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetAllCurationQuestionService implements GetAllCurationQuestionUseCase {

    private static final int PHOTOS_PER_STEP = 4;

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final QuestionRepository questionRepository;
    private final PhotoRepository photoRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetAllCurationQuestionResult getAllCurationQuestions(Long userId) {
        User user = validateAndGetLoginUser(userId);

        List<Question> questions = getQuestions();
        validateQuestionsExist(questions);

        Gender gender = getUserGender(user);
        List<Photo> photos = new ArrayList<>(photoRepository.findAllByGender(gender));
        Collections.shuffle(photos);

        List<GetCurationQuestionResult> results = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            int startIndex = i * PHOTOS_PER_STEP;
            List<Photo> stepPhotos = photos.subList(startIndex, startIndex + PHOTOS_PER_STEP);
            List<GetPhotoResult> photoResults = mapPhotosToResults(stepPhotos);
            results.add(GetCurationQuestionResult.of(questions.get(i), photoResults));
        }

        return GetAllCurationQuestionResult.of(results);
    }

    private User validateAndGetLoginUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CurationException(CurationErrorCode.CURATION_LOGIN_REQUIRED));
    }

    private List<Question> getQuestions() {
        return questionRepository
            .findAllByQuestionDomainOrderByStep(QuestionDomain.MOOD_CURATION);
    }

    private static void validateQuestionsExist(List<Question> questions) {
        if (questions.isEmpty()) {
            throw new CurationException(CurationErrorCode.QUESTION_NOT_FOUND);
        }
    }

    private Gender getUserGender(User user) {
        Onboarding onboarding = onboardingRepository.findByUser(user)
            .orElseThrow(
                () -> new CurationException(CurationErrorCode.CURATIOON_ONBOARDING_REQUIRED)
            );

        return onboarding.getGender();
    }

    private List<GetPhotoResult> mapPhotosToResults(List<Photo> photos) {
        List<GetPhotoResult> results = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            results.add(
                new GetPhotoResult(
                    photo.getId(),
                    cloudFrontDomain + photo.getImageUrl(),
                    i + 1
                )
            );
        }
        return results;
    }
}
