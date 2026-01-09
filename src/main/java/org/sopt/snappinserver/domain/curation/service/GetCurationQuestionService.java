package org.sopt.snappinserver.domain.curation.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetPhotoResult;
import org.sopt.snappinserver.domain.curation.service.usecase.GetCurationQuestionUseCase;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photo.repository.PhotoRepository;
import org.sopt.snappinserver.domain.question.domain.entity.Question;
import org.sopt.snappinserver.domain.question.domain.entity.QuestionPhoto;
import org.sopt.snappinserver.domain.question.domain.enums.QuestionDomain;
import org.sopt.snappinserver.domain.question.repository.QuestionPhotoRepository;
import org.sopt.snappinserver.domain.question.repository.QuestionRepository;
import org.sopt.snappinserver.global.response.S3Service;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetCurationQuestionService implements GetCurationQuestionUseCase {

    private static final int MIN_STEP = 1;
    private static final int MAX_STEP = 5;

    private final QuestionRepository questionRepository;
    private final QuestionPhotoRepository questionPhotoRepository;
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    @Override
    public GetCurationQuestionResult getCurationQuestionPhotos(Long userId, Integer step) {
        validateLoginUser(userId);
        validateStep(step);

        Question question = getMoodCurationQuestionByStep(step);
        List<QuestionPhoto> questionPhotos = questionPhotoRepository
            .findQuestionPhotoByQuestion(question);
        List<GetPhotoResult> photos = getGetPhotoResults(questionPhotos);

        return GetCurationQuestionResult.of(question, photos);
    }

    private static void validateLoginUser(Long userId) {
        if (userId == null) {
            throw new CurationException(CurationErrorCode.CURATION_LOGIN_REQUIRED);
        }
    }

    private void validateStep(Integer step) {
        validateStepExists(step);
        validateStepRange(step);
    }

    private static void validateStepExists(Integer step) {
        if (step == null) {
            throw new CurationException(CurationErrorCode.STEP_REQUIRED);
        }
    }

    private void validateStepRange(Integer step) {
        if (step < MIN_STEP || step > MAX_STEP) {
            throw new CurationException(CurationErrorCode.INVALID_STEP_RANGE);
        }
    }

    private Question getMoodCurationQuestionByStep(Integer step) {
        return questionRepository.findByQuestionDomainAndStep(
                QuestionDomain.MOOD_CURATION,
                step
            )
            .orElseThrow(() -> new CurationException(CurationErrorCode.QUESTION_NOT_FOUND));
    }

    private List<GetPhotoResult> getGetPhotoResults(List<QuestionPhoto> questionPhotos) {
        return questionPhotos.stream()
            .map(questionPhoto -> {
                Photo photo = questionPhoto.getPhoto();
                String presignedUrl = s3Service.getPresignedUrl(photo.getImageUrl());
                return new GetPhotoResult(
                    photo.getId(),
                    presignedUrl,
                    questionPhoto.getDisplayOrder()
                );
            })
            .toList();
    }
}
