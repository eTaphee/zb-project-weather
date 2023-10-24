package kr.co.zerobase.weather.dto;

import java.time.LocalDate;
import kr.co.zerobase.weather.domain.Diary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryDto {

    private final String weather;

    private final String icon;

    private final double temperature;

    private final String text;

    private final LocalDate date;

    public static DiaryDto fromEntity(Diary diary) {
        return DiaryDto.builder()
            .weather(diary.getWeather())
            .icon(diary.getIcon())
            .temperature(diary.getTemperature())
            .text(diary.getText())
            .date(diary.getDate())
            .build();
    }
}
