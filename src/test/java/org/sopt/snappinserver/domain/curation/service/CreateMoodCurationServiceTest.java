package org.sopt.snappinserver.domain.curation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.curation.repository.CurationRepository;
import org.sopt.snappinserver.domain.curation.service.dto.request.CreateMoodCurationCommand;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodResult;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.photo.domain.entity.PhotoMood;
import org.sopt.snappinserver.domain.photo.repository.PhotoMoodRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CreateMoodCurationServiceTest {

    @InjectMocks
    private CreateMoodCurationService createMoodCurationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhotoMoodRepository photoMoodRepository;

    @Mock
    private CurationRepository curationRepository;

    private User mockUser;

    private CreateMoodCurationCommand command;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        command = new CreateMoodCurationCommand(
            1L,
            List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L)
        );
    }

    @Test
    @DisplayName("무드 순위(1~3위)가 총점 내림차순으로 올바르게 정렬되며 3번 저장되는 테스트")
    void createMoodCuration_OrderedByScoreAndSaved() {
        // [Given] 총점이 다른 3개 무드 생성 (moodA: 50점, moodB: 25점, moodC: 5점)
        Mood moodA = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        Mood moodB = Mood.create(MoodCategory.STYLE, "아날로그", "설명", List.of(0.2f));
        Mood moodC = Mood.create(MoodCategory.STYLE, "Y2K", "설명", List.of(0.3f));

        List<PhotoMood> mockPhotoMoods = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodA, 1, 10.0f));
        }
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodB, 1, 5.0f));
        }
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodC, 1, 1.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);

        // [When]
        CreateMoodCurationResult result = createMoodCurationService.saveMoodCurationResult(command);

        // [Then] 결과 3개, 점수 내림차순 정렬, DB 저장 3회 확인
        assertThat(result.moods()).hasSize(3);
        assertThat(result.moods().get(0).name()).isEqualTo("디지털");
        assertThat(result.moods().get(1).name()).isEqualTo("아날로그");
        assertThat(result.moods().get(2).name()).isEqualTo("Y2K");
        verify(curationRepository, times(3)).save(any(Curation.class));
    }

    @Test
    @DisplayName("큐레이션 저장 시 rank 값이 1, 2, 3으로 순서대로 저장되는 테스트")
    void createMoodCuration_SavesWithCorrectRank() {
        // [Given] 임의의 3개 무드 생성
        Mood moodA = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        Mood moodB = Mood.create(MoodCategory.STYLE, "아날로그", "설명", List.of(0.2f));
        Mood moodC = Mood.create(MoodCategory.STYLE, "Y2K", "설명", List.of(0.3f));

        List<PhotoMood> mockPhotoMoods = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodA, 1, 10.0f));
        }
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodB, 1, 5.0f));
        }
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodC, 1, 1.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);

        ArgumentCaptor<Curation> curationCaptor = ArgumentCaptor.forClass(Curation.class);

        // [When]
        createMoodCurationService.saveMoodCurationResult(command);

        // [Then] 저장된 Curation의 rank 값이 1, 2, 3인지 순서대로 확인
        verify(curationRepository, times(3)).save(curationCaptor.capture());
        List<Curation> savedCurations = curationCaptor.getAllValues();
        assertThat(savedCurations.get(0).getRank()).isEqualTo(1);
        assertThat(savedCurations.get(1).getRank()).isEqualTo(2);
        assertThat(savedCurations.get(2).getRank()).isEqualTo(3);
    }

    @Test
    @DisplayName("저장된 Curation에 올바른 User와 Mood가 포함되는 테스트")
    void createMoodCuration_SavesWithCorrectUserAndMood() {
        // [Given] 임의의 3개 무드 생성
        Mood moodA = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        Mood moodB = Mood.create(MoodCategory.STYLE, "아날로그", "설명", List.of(0.2f));
        Mood moodC = Mood.create(MoodCategory.STYLE, "Y2K", "설명", List.of(0.3f));

        List<PhotoMood> mockPhotoMoods = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodA, 1, 10.0f));
        }
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodB, 1, 5.0f));
        }
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodC, 1, 1.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);

        ArgumentCaptor<Curation> curationCaptor = ArgumentCaptor.forClass(Curation.class);

        // [When]
        createMoodCurationService.saveMoodCurationResult(command);

        // [Then] 저장된 Curation의 user, mood 필드 검증
        verify(curationRepository, times(3)).save(curationCaptor.capture());
        List<Curation> savedCurations = curationCaptor.getAllValues();
        assertThat(savedCurations.get(0).getUser()).isEqualTo(mockUser);
        assertThat(savedCurations.get(0).getMood()).isEqualTo(moodA);
        assertThat(savedCurations.get(1).getMood()).isEqualTo(moodB);
        assertThat(savedCurations.get(2).getMood()).isEqualTo(moodC);
    }

    @Test
    @DisplayName("distinct 무드가 3개 미만이면 해당 개수만큼만 저장되는 테스트")
    void createMoodCuration_LessThan3DistinctMoods_SavesOnlyAvailable() {
        // [Given] PhotoMood는 15개이지만 distinct 무드가 2개뿐인 상황 생성
        Mood moodA = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        Mood moodB = Mood.create(MoodCategory.STYLE, "아날로그", "설명", List.of(0.2f));

        List<PhotoMood> mockPhotoMoods = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodA, 1, 10.0f));
        }
        for (int i = 0; i < 7; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodB, 1, 5.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);

        // [When]
        CreateMoodCurationResult result = createMoodCurationService.saveMoodCurationResult(command);

        // [Then] 결과값이 2개이며 DB 저장도 2번만 일어났는지 확인
        assertThat(result.moods()).hasSize(2);
        verify(curationRepository, times(2)).save(any(Curation.class));
    }

    @Test
    @DisplayName("distinct 무드가 3개 초과이면 상위 3개만 저장되는 테스트")
    void createMoodCuration_MoreThan3DistinctMoods_SavesOnlyTop3() {
        // [Given] 총점이 다른 4개 무드 생성 (moodA: 50, moodB: 40, moodC: 30, moodD: 15)
        Mood moodA = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        Mood moodB = Mood.create(MoodCategory.STYLE, "아날로그", "설명", List.of(0.2f));
        Mood moodC = Mood.create(MoodCategory.STYLE, "Y2K", "설명", List.of(0.3f));
        Mood moodD = Mood.create(MoodCategory.STYLE, "빈티지", "설명", List.of(0.4f));

        // 4개 무드로 합계 15개 PhotoMood 구성 (5+4+3+3=15)
        List<PhotoMood> mockPhotoMoods = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodA, 1, 10.0f));
        }
        for (int i = 0; i < 4; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodB, 1, 10.0f));
        }
        for (int i = 0; i < 3; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodC, 1, 10.0f));
        }
        for (int i = 0; i < 3; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, moodD, 1, 5.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);

        // [When]
        CreateMoodCurationResult result = createMoodCurationService.saveMoodCurationResult(command);

        // [Then] 결과값이 3개이며, 최하위 moodD는 포함되지 않는지 확인
        assertThat(result.moods()).hasSize(3);
        assertThat(result.moods().stream().map(CreateMoodResult::name))
            .doesNotContain("빈티지");
        verify(curationRepository, times(3)).save(any(Curation.class));
    }

    @Test
    @DisplayName("결과에 사용자 이름이 올바르게 포함되는 테스트")
    void createMoodCuration_ResultContainsUserName() {
        // [Given]
        Mood mood = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        List<PhotoMood> mockPhotoMoods = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            mockPhotoMoods.add(PhotoMood.create(null, mood, 1, 10.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);
        when(mockUser.getName()).thenReturn("테스트유저");

        // [When]
        CreateMoodCurationResult result = createMoodCurationService.saveMoodCurationResult(command);

        // [Then] result.name이 user 이름과 일치하는지 확인
        assertThat(result.name()).isEqualTo("테스트유저");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 요청 시 실패 테스트")
    void createMoodCuration_Fail_UserNotFound() {
        // [Given] 존재하지 않는 userId로 요청
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // [When & Then] USER_NOT_FOUND 예외 발생 확인
        assertThatThrownBy(() -> createMoodCurationService.saveMoodCurationResult(command))
            .isInstanceOf(CurationException.class)
            .hasMessage(CurationErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("반환된 PhotoMood 수가 15개가 아닌 경우 실패 테스트")
    void createMoodCuration_Fail_PhotoIdNotFound() {
        // [Given] 15개 photoId를 요청했지만 일부가 DB에 없어 12개만 반환된 상황 생성
        Mood mood = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        List<PhotoMood> incompletePhotoMoods = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            incompletePhotoMoods.add(PhotoMood.create(null, mood, 1, 10.0f));
        }

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(incompletePhotoMoods);

        // [When & Then] PHOTO_ID_NOT_FOUND 예외 발생 확인
        assertThatThrownBy(() -> createMoodCurationService.saveMoodCurationResult(command))
            .isInstanceOf(CurationException.class)
            .hasMessage(CurationErrorCode.PHOTO_ID_NOT_FOUND.getMessage());
    }
}
