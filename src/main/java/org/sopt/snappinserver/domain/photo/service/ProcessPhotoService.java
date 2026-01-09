package org.sopt.snappinserver.domain.photo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository.MoodWithScore;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photo.domain.entity.PhotoMood;
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

    private static final List<Set<String>> OPPOSITE_PAIRS = List.of(
        Set.of("내추럴", "연출된"),
        Set.of("아날로그", "디지털")
    );

    private final PhotoRepository photoRepository;
    private final MoodRepository moodRepository;
    private final PhotoMoodRepository photoMoodRepository;

    @Override
    public void linkPhotoWithMoodTags(PhotoProcessCommand photoProcessCommand) {
        Photo photo = Photo.create(photoProcessCommand.imageUrl(), photoProcessCommand.embedding());
        photoRepository.save(photo);

        List<MoodWithScore> candidates = moodRepository.findCandidates(
            photoProcessCommand.embedding()
        );
        List<MoodWithScore> selectedMoods = filterTop3Moods(candidates);

        int rank = 1;
        for (MoodWithScore result : selectedMoods) {
            Mood moodProxy = moodRepository.getReferenceById(result.getId());
            PhotoMood photoMood = PhotoMood.create(photo, moodProxy, rank++, result.getScore());
            photoMoodRepository.save(photoMood);
        }
    }

    private List<MoodWithScore> filterTop3Moods(List<MoodWithScore> candidates) {
        List<MoodWithScore> top3 = new ArrayList<>();

        for (MoodWithScore candidate : candidates) {
            if (top3.size() >= 3) {
                break;
            }

            String currentMoodName = candidate.getName();
            boolean isConflict = false;
            for (MoodWithScore selected : top3) {
                String selectedName = selected.getName();
                if (isOpposite(currentMoodName, selectedName)) {
                    isConflict = true;
                    break;
                }
            }
            if (!isConflict) {
                top3.add(candidate);
            }
        }
        return top3;
    }

    private boolean isOpposite(String firstTag, String secondTag) {
        for (Set<String> pair : OPPOSITE_PAIRS) {
            if (pair.contains(firstTag) && pair.contains(secondTag)) {
                return true;
            }
        }
        return false;
    }
}
