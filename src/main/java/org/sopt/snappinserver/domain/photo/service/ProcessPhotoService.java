package org.sopt.snappinserver.domain.photo.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.policy.MoodSelector;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.mood.repository.MoodVectorRepository;
import org.sopt.snappinserver.domain.mood.repository.MoodWithScore;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photo.domain.entity.PhotoMood;
import org.sopt.snappinserver.domain.photo.domain.exception.PhotoErrorCode;
import org.sopt.snappinserver.domain.photo.domain.exception.PhotoException;
import org.sopt.snappinserver.domain.photo.repository.PhotoMoodRepository;
import org.sopt.snappinserver.domain.photo.repository.PhotoRepository;
import org.sopt.snappinserver.domain.photo.service.dto.request.PhotoProcessCommand;
import org.sopt.snappinserver.domain.photo.service.usecase.ProcessPhotoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProcessPhotoService implements ProcessPhotoUseCase {

    private static final int LAST_RANK = 3;

    private final PhotoRepository photoRepository;
    private final MoodVectorRepository moodVectorRepository;
    private final MoodRepository moodRepository;
    private final PhotoMoodRepository photoMoodRepository;
    private final MoodSelector moodSelector;

    @Override
    public void linkPhotoWithMoodTags(PhotoProcessCommand photoProcessCommand) {
        validateImageUrlUnique(photoProcessCommand);
        log.info("성공1");
        Photo photo = photoRepository.save(Photo.create(photoProcessCommand.imageUrl()));
        log.info("성공2");

        List<MoodWithScore> candidates = moodVectorRepository.findTopCandidates(
            toVectorLiteral(photoProcessCommand.embedding())
        );
        log.info("성공3");
        List<MoodWithScore> selectedScores = moodSelector.selectTop3(LAST_RANK, candidates);
        log.info("성공4");
        List<Mood> selectedMood = getSelectedMood(selectedScores);
        log.info("성공5");
        List<PhotoMood> linkedPhotoMoods = photo.linkMoods(selectedMood, selectedScores);
        log.info("성공6");

        photoMoodRepository.saveAll(linkedPhotoMoods);
        log.info("성공7");
    }

    private String toVectorLiteral(List<Float> embedding) {
        return "[" + embedding.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(",")) + "]";
    }

    private List<Mood> getSelectedMood(List<MoodWithScore> selected) {
        return selected.stream()
            .map(m -> moodRepository.getReferenceById(m.id()))
            .toList();
    }

    private void validateImageUrlUnique(PhotoProcessCommand photoProcessCommand) {
        if (photoRepository.existsByImageUrl(photoProcessCommand.imageUrl())) {
            throw new PhotoException(PhotoErrorCode.IMAGE_URL_ALREADY_SAVED);
        }
    }
}
