package org.sopt.snappinserver.domain.curation.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.curation.repository.CurationRepository;
import org.sopt.snappinserver.domain.curation.service.dto.request.CreateMoodCurationCommand;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;
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
    @DisplayName("상위 3개 무드 추출 및 저장 성공 테스트")
    void createMoodCuration_Success() {
        // [Given] 테스트 시 필요한 데이터 생성
        // 1. 임의의 상위 3개 결과 무드 생성
        Mood moodA = Mood.create(MoodCategory.STYLE, "디지털", "설명", List.of(0.1f));
        Mood moodB = Mood.create(MoodCategory.STYLE, "아날로그", "설명", List.of(0.2f));
        Mood moodC = Mood.create(MoodCategory.STYLE, "Y2K", "설명", List.of(0.3f));

        // 2. 위에서 만든 세 무드와 연결된 PhotoMood 리스트 임의 생성
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

        // 3. Mockito에게 행동 지시
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(photoMoodRepository.findAllByPhotoIdIn(anyList())).thenReturn(mockPhotoMoods);

        // [When] 테스트할 서비스 메서드 호출
        CreateMoodCurationResult result = createMoodCurationService.saveMoodCurationResult(command);

        // [Then] 결과 검증
        // 1. 결과값이 3개인지 확인
        assertThat(result.moods()).hasSize(3);

        // 2. 점수가 가장 높은 moodA가 0번째 인덱스(1위)인지 확인
        assertThat(result.moods().get(0).name()).isEqualTo("디지털");

        // 3. DB 저장이 3번 일어났는지 확인
        verify(curationRepository, times(3)).save(any(Curation.class));
    }
}