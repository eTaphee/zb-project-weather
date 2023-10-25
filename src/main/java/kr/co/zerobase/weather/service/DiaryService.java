package kr.co.zerobase.weather.service;

import static kr.co.zerobase.weather.type.ErrorCode.COULD_NOT_GET_WEATHER_DATA;
import static kr.co.zerobase.weather.type.ErrorCode.DIARY_NOT_FOUND;
import static kr.co.zerobase.weather.type.ErrorCode.INVALID_DATE;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.zerobase.weather.domain.DateWeather;
import kr.co.zerobase.weather.domain.Diary;
import kr.co.zerobase.weather.dto.DiaryDto;
import kr.co.zerobase.weather.dto.WeatherResponseDto;
import kr.co.zerobase.weather.exception.WeatherException;
import kr.co.zerobase.weather.repository.DateWeatherRepository;
import kr.co.zerobase.weather.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    @Transactional
    public void createDiary(LocalDate date, String text) {
        validateDate(date);

        diaryRepository.save(Diary.createWithDateWeather(text, date, getDateWeather(date)));
    }

    @Transactional(readOnly = true)
    public List<DiaryDto> readDiary(LocalDate date) {
        validateDate(date);

        return diaryRepository.findAllByDate(date)
            .stream()
            .map(DiaryDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DiaryDto> readDiaries(LocalDate startDate, LocalDate endDate) {
        validateDateBetween(startDate, endDate);

        return diaryRepository.findAllByDateBetween(startDate, endDate)
            .stream()
            .map(DiaryDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        validateDate(date);

        Diary diary = diaryRepository.getFirstByDate(date)
            .orElseThrow(() -> new WeatherException(DIARY_NOT_FOUND));

        diary.setText(text);
        diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        validateDate(date);

        diaryRepository.deleteAllByDate(date);
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveDateWeather() {
        log.info("saveDateWeather() 스케줄이 실행됩니다.");
        getWeatherFromApi();
        log.info("saveDateWeather() 스케줄이 완료됐습니다.");
    }

    private DateWeather getDateWeather(LocalDate date) {
        return dateWeatherRepository.findById(date)
            .orElseGet(() ->
                getWeatherFromApi()
                    .orElseThrow(() ->
                        new WeatherException(COULD_NOT_GET_WEATHER_DATA)));
    }

    private Optional<DateWeather> getWeatherFromApi() {
        URI uri = UriComponentsBuilder
            .fromHttpUrl("https://api.openweathermap.org/data/2.5/weather")
            .queryParam("q", "seoul")
            .queryParam("appid", apiKey)
            .queryParam("units", "metric")
            .build()
            .toUri();

        ResponseEntity<WeatherResponseDto> response = restTemplate.getForEntity(uri,
            WeatherResponseDto.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Optional.empty();
        }

        return Optional.of(dateWeatherRepository.save(DateWeather.from(
            Objects.requireNonNull(response.getBody()))));
    }

    private static void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.ofYearDay(2100, 1)) ||
            date.isBefore(LocalDate.ofYearDay(1990, 1))) {
            throw new WeatherException(INVALID_DATE);
        }
    }

    private static void validateDateBetween(LocalDate startDate, LocalDate endDate) {
        validateDate(startDate);
        validateDate(endDate);

        if (startDate.isAfter(endDate)) {
            throw new WeatherException(INVALID_DATE);
        }
    }
}
