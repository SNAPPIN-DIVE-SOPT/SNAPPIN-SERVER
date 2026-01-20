package org.sopt.snappinserver.domain.curation.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetAllCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetPhotoResult;
import org.sopt.snappinserver.domain.curation.service.usecase.GetAllCurationQuestionUseCase;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.question.domain.entity.Question;
import org.sopt.snappinserver.domain.question.domain.entity.QuestionPhoto;
import org.sopt.snappinserver.domain.question.domain.enums.QuestionDomain;
import org.sopt.snappinserver.domain.question.repository.QuestionPhotoRepository;
import org.sopt.snappinserver.domain.question.repository.QuestionRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetAllCurationQuestionService implements GetAllCurationQuestionUseCase {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionPhotoRepository questionPhotoRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetAllCurationQuestionResult getAllCurationQuestions(Long userId) {
        validateLoginUser(userId);

        List<Question> questions = getQuestions();
        validateQuestionsExist(questions);
        List<QuestionPhoto> questionPhotos = questionPhotoRepository.findAllByQuestionIn(questions);

        Map<Long, List<QuestionPhoto>> photosByQuestionId =
            questionPhotos.stream()
                .collect(Collectors.groupingBy(
                    qp -> qp.getQuestion().getId()
                ));

        List<GetCurationQuestionResult> results =
            questions.stream()
                .map(question -> GetCurationQuestionResult.of(
                    question,
                    mapToPhotoResults(
                        photosByQuestionId.getOrDefault(question.getId(), List.of())
                    )
                ))
                .toList();

        return GetAllCurationQuestionResult.of(results);
    }

    private void validateLoginUser(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new CurationException(CurationErrorCode.CURATION_LOGIN_REQUIRED);
        }
    }

    private List<Question> getQuestions() {
        return questionRepository
            .findAllByQuestionDomainOrderByStep(QuestionDomain.MOOD_CURATION);
    }

    private static void validateQuestionsExist(List<Question> questions) {
        if(questions.isEmpty()) {
            throw new CurationException(CurationErrorCode.QUESTION_NOT_FOUND);
        }
    }

    private List<GetPhotoResult> mapToPhotoResults(List<QuestionPhoto> questionPhotos) {
        return questionPhotos.stream()
            .map(questionPhoto -> {
                Photo photo = questionPhoto.getPhoto();
                return new GetPhotoResult(
                    photo.getId(),
                    cloudFrontDomain.concat(photo.getImageUrl()),
                    questionPhoto.getDisplayOrder()
                );
            })
            .toList();
    }
}
