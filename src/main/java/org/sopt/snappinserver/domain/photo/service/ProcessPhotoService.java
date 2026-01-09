package org.sopt.snappinserver.domain.photo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.policy.MoodSelector;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository.MoodWithScore;
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
public class ProcessPhotoService implements ProcessPhotoUseCase {

    private static final int LAST_RANK = 3;

    private final PhotoRepository photoRepository;
    private final MoodRepository moodRepository;
    private final PhotoMoodRepository photoMoodRepository;
    private final MoodSelector moodSelector;

    @Override
    public void linkPhotoWithMoodTags(PhotoProcessCommand photoProcessCommand) {
        validateImageUrlUnique(photoProcessCommand);
        Photo photo = photoRepository.save(
            Photo.create(photoProcessCommand.imageUrl(), photoProcessCommand.embedding())
        );

        List<MoodWithScore> candidates = moodRepository.findCandidates(
            photoProcessCommand.embedding()
        );
        List<MoodWithScore> selectedScores = moodSelector.selectTop3(LAST_RANK, candidates);
        List<Mood> selectedMood = getSelectedMood(selectedScores);
        List<PhotoMood> linkedPhotoMoods = photo.linkMoods(selectedMood, selectedScores);

        photoMoodRepository.saveAll(linkedPhotoMoods);
    }

    private List<Mood> getSelectedMood(List<MoodWithScore> selected) {
        return selected.stream()
            .map(m -> moodRepository.getReferenceById(m.getId()))
            .toList();
    }

    private void validateImageUrlUnique(PhotoProcessCommand photoProcessCommand) {
        if(photoRepository.existsByImageUrl(photoProcessCommand.imageUrl())) {
            throw new PhotoException(PhotoErrorCode.IMAGE_URL_ALREADY_SAVED);
        }
    }
}
