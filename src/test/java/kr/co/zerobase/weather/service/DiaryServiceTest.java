package kr.co.zerobase.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.co.zerobase.weather.domain.DateWeather;
import kr.co.zerobase.weather.domain.Diary;
import kr.co.zerobase.weather.dto.DiaryDto;
import kr.co.zerobase.weather.repository.DateWeatherRepository;
import kr.co.zerobase.weather.repository.DiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @InjectMocks
    private DiaryService diaryService;

    @Test
    @DisplayName("일기 생성 성공")
    void successCreateDiary() {
        // given
        Diary diary = Diary.builder()
            .weather("rain")
            .icon("rain_icon")
            .text("contents")
            .temperature(12)
            .build();

        Optional<DateWeather> dateWeather = Optional.of(DateWeather.builder()
            .weather(diary.getWeather())
            .icon(diary.getIcon())
            .temperature(diary.getTemperature())
            .build());

        given(diaryRepository.save(any())).willReturn(diary);

        given(dateWeatherRepository.findById(any())).willReturn(dateWeather);

        // when
        diaryService.createDiary(LocalDate.now(), "contents");

        // then
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);
        verify(diaryRepository, times(1)).save(captor.capture());
        assertEquals("rain", captor.getValue().getWeather());
        assertEquals("rain_icon", captor.getValue().getIcon());
        assertEquals("contents", captor.getValue().getText());
        assertEquals(12, captor.getValue().getTemperature());
    }

    @Test
    @DisplayName("일기 수정 성공")
    void successUpdateDiary() {
        // given
        given(diaryRepository.getFirstByDate(any()))
            .willReturn(Optional.ofNullable(Diary.builder()
                .weather("rain")
                .icon("rain_icon")
                .text("before contents")
                .temperature(12)
                .build()));

        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        // when
        diaryService.updateDiary(LocalDate.now(), "contents");

        // then
        verify(diaryRepository, times(1)).save(captor.capture());
        assertEquals("rain", captor.getValue().getWeather());
        assertEquals("contents", captor.getValue().getText());
    }

    @Test
    @DisplayName("일기 삭제 성공")
    void successDeleteDiary() {
        // when
        ArgumentCaptor<LocalDate> captor = ArgumentCaptor.forClass(LocalDate.class);
        diaryService.deleteDiary(LocalDate.now());

        // then
        verify(diaryRepository, times(1)).deleteAllByDate(captor.capture());
    }

    @Test
    @DisplayName("일기 조회 성공")
    void successReadDiary() {
        given(diaryRepository.findAllByDate(any()))
            .willReturn(Collections.singletonList(Diary.builder()
                .text("일기내용")
                .icon("icon")
                .weather("rain")
                .temperature(14.5)
                .date(LocalDate.of(2023, 1, 1))
                .build()));

        // when
        ArgumentCaptor<LocalDate> captor = ArgumentCaptor.forClass(LocalDate.class);
        List<DiaryDto> diaryDtos = diaryService.readDiary(LocalDate.now());

        // then
        verify(diaryRepository, times(1)).findAllByDate(captor.capture());
        assertEquals(1, diaryDtos.size());
        assertEquals("일기내용", diaryDtos.get(0).getText());
        assertEquals("icon", diaryDtos.get(0).getIcon());
        assertEquals("rain", diaryDtos.get(0).getWeather());
        assertEquals(14.5, diaryDtos.get(0).getTemperature());
        assertEquals(LocalDate.of(2023, 1, 1), diaryDtos.get(0).getDate());
    }

    @Test
    @DisplayName("일기 범위 조회 성공")
    void successReadDiaries() {
        given(diaryRepository.findAllByDateBetween(any(), any()))
            .willReturn(Collections.singletonList(Diary.builder()
                .text("일기내용")
                .icon("icon")
                .weather("rain")
                .temperature(14.5)
                .date(LocalDate.of(2023, 1, 1))
                .build()));

        // when
        ArgumentCaptor<LocalDate> captor = ArgumentCaptor.forClass(LocalDate.class);
        List<DiaryDto> diaryDtos = diaryService.readDiaries(LocalDate.now(), LocalDate.now());

        // then
        verify(diaryRepository, times(1)).findAllByDateBetween(captor.capture(), captor.capture());
        assertEquals(1, diaryDtos.size());
        assertEquals("일기내용", diaryDtos.get(0).getText());
        assertEquals("icon", diaryDtos.get(0).getIcon());
        assertEquals("rain", diaryDtos.get(0).getWeather());
        assertEquals(14.5, diaryDtos.get(0).getTemperature());
    }
}