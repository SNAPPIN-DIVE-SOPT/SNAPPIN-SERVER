package org.sopt.snappinserver.domain.curation.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.curation.repository.CurationRepository;
import org.sopt.snappinserver.domain.curation.service.dto.request.CreateMoodCurationCommand;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;
import org.sopt.snappinserver.domain.curation.service.usecase.CreateMoodCurationUseCase;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.photo.domain.entity.PhotoMood;
import org.sopt.snappinserver.domain.photo.repository.PhotoMoodRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateMoodCurationService implements CreateMoodCurationUseCase {

    private final UserRepository userRepository;
    private final PhotoMoodRepository photoMoodRepository;
    private final CurationRepository curationRepository;

    @Override
    public CreateMoodCurationResult saveMoodCurationResult(
        CreateMoodCurationCommand command
    ) {
        validateLoginUser(command);
        User user = getUser(command);

        List<PhotoMood> photoMoods = getPhotoMoods(command);
        Map<Mood, Double> moodScores = getMoodScores(photoMoods);
        List<Mood> top3Moods = getTop3Moods(moodScores);

        saveCurationResult(top3Moods, user);

        return CreateMoodCurationResult.of(user, top3Moods);
    }

    private User getUser(CreateMoodCurationCommand command) {
        return userRepository.findById(command.userId())
            .orElseThrow(() -> new CurationException(CurationErrorCode.USER_NOT_FOUND));
    }

    private Map<Mood, Double> getMoodScores(List<PhotoMood> photoMoods) {
        return photoMoods.stream()
            .collect(Collectors.groupingBy(
                PhotoMood::getMood,
                Collectors.summingDouble(PhotoMood::getScore)
            ));
    }

    private List<Mood> getTop3Moods(Map<Mood, Double> moodScores) {
        return moodScores.entrySet().stream()
            .sorted(Map.Entry.<Mood, Double>comparingByValue().reversed())
            .limit(3)
            .map(Map.Entry::getKey)
            .toList();
    }

    private void saveCurationResult(List<Mood> top3Moods, User user) {
        for (int i = 0; i < top3Moods.size(); i++) {
            Mood mood = top3Moods.get(i);
            Curation curation = Curation.create(user, mood, i + 1);
            curationRepository.save(curation);
        }
    }

    private void validateLoginUser(CreateMoodCurationCommand command) {
        if (command.userId() == null) {
            throw new CurationException(CurationErrorCode.CURATION_LOGIN_REQUIRED);
        }
    }

    private List<PhotoMood> getPhotoMoods(CreateMoodCurationCommand command) {
        return photoMoodRepository.findAllByPhotoIdIn(command.photoIds());
    }
}