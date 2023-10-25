package kr.co.zerobase.weather.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "날씨", example = "Rain", required = true)
    private final String weather;

    @ApiModelProperty(value = "날씨 아이콘", example = "04n", required = true)
    private final String icon;

    @ApiModelProperty(value = "섭씨 온도", example = "15.5", required = true)
    private final double temperature;

    @ApiModelProperty(value = "내용", example = "오늘 날씨 비온다.", required = true)
    private final String text;

    @ApiModelProperty(value = "날짜", example = "2023-01-01", required = true)
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
