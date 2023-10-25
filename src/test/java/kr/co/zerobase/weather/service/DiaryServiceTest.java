package kr.co.zerobase.weather.service;

import static kr.co.zerobase.weather.type.ErrorCode.COULD_NOT_GET_WEATHER_DATA;
import static kr.co.zerobase.weather.type.ErrorCode.DIARY_NOT_FOUND;
import static kr.co.zerobase.weather.type.ErrorCode.INVALID_DATE;
import static kr.co.zerobase.weather.type.ErrorCode.INVALID_DATE_END_DATE_IS_BEFORE_START_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import kr.co.zerobase.weather.dto.WeatherResponseDto;
import kr.co.zerobase.weather.exception.WeatherException;
import kr.co.zerobase.weather.repository.DateWeatherRepository;
import kr.co.zerobase.weather.repository.DiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @Mock
    private RestTemplate restTemplate;

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
    @DisplayName("일기 생성 실패 - 유효하지 않은 날짜")
    void failCreateDiary_InvalidDate() {
        // given

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.createDiary(LocalDate.MIN, "일기내용"));

        // then
        assertEquals(INVALID_DATE, weatherException.getErrorCode());
    }

    @Test
    @DisplayName("일기 생성 실패 - 날씨 데이터 가져올 수 없음")
    void failCreateDiary_CouldNotGetWeatherData() {
        // given
        given(dateWeatherRepository.findById(any()))
            .willReturn(Optional.empty());

        given(restTemplate.getForEntity(any(), any()))
            .willReturn(ResponseEntity.badRequest().build());

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.createDiary(LocalDate.now(), "일기내용"));

        // then
        assertEquals(COULD_NOT_GET_WEATHER_DATA, weatherException.getErrorCode());
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
    @DisplayName("일기 수정 실패 - 유효하지 않은 날짜")
    void failUpdateDiary_InvalidDate() {
        // given

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.updateDiary(LocalDate.MIN, "일기내용"));

        // then
        assertEquals(INVALID_DATE, weatherException.getErrorCode());
    }

    @Test
    @DisplayName("일기 수정 실패 - 일기 없음")
    void failUpdateDiary_DiaryNotFound() {
        // given
        given(diaryRepository.getFirstByDate(any())).willReturn(Optional.empty());

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.updateDiary(LocalDate.now(), "일기내용"));

        // then
        assertEquals(DIARY_NOT_FOUND, weatherException.getErrorCode());
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
    @DisplayName("일기 삭제 실패 - 유효하지 않은 날짜")
    void failDeleteDiary_InvalidDate() {
        // given

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.deleteDiary(LocalDate.MIN));

        // then
        assertEquals(INVALID_DATE, weatherException.getErrorCode());
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
    @DisplayName("일기 조회 실패 - 유효하지 않은 날짜")
    void failReadDiary_InvalidDate() {
        // given

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.readDiary(LocalDate.MIN));

        // then
        assertEquals(INVALID_DATE, weatherException.getErrorCode());
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

    @Test
    @DisplayName("일기 범위 조회 실패 - 유효하지 않은 날짜")
    void failReadDiaries_InvalidDate() {
        // given

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.readDiaries(LocalDate.MIN, LocalDate.MAX));

        // then
        assertEquals(INVALID_DATE, weatherException.getErrorCode());
    }

    @Test
    @DisplayName("일기 범위 조회 실패 - endDate가 startDate보다 이전 일 때")
    void failReadDiaries_endDateIsBeforeStartDate() {
        // given

        // when
        WeatherException weatherException = assertThrows(WeatherException.class,
            () -> diaryService.readDiaries(LocalDate.now(), LocalDate.now().minusDays(1)));

        // then
        assertEquals(INVALID_DATE_END_DATE_IS_BEFORE_START_DATE, weatherException.getErrorCode());
    }

    @Test
    @DisplayName("날씨 데이터 저장")
    void successSaveDateWeather() {
        // given
        WeatherResponseDto dto = WeatherResponseDto.builder()
            .weather("clouds")
            .icon("clouds_icon")
            .temperature(13.5)
            .build();

        DateWeather dateWeather = DateWeather.from(dto);

        given(restTemplate.getForEntity(any(), any()))
            .willReturn(ResponseEntity.ok().body(dto));

        given(dateWeatherRepository.save(any()))
            .willReturn(dateWeather);

        // when
        diaryService.saveDateWeather();

        // then
        ArgumentCaptor<DateWeather> captor = ArgumentCaptor.forClass(DateWeather.class);
        verify(dateWeatherRepository, times(1)).save(captor.capture());
        assertEquals("clouds", captor.getValue().getWeather());
        assertEquals("clouds_icon", captor.getValue().getIcon());
        assertEquals(13.5, captor.getValue().getTemperature());
    }
}