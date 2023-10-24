package kr.co.zerobase.weather.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kr.co.zerobase.weather.type.ErrorCode.INVALID_REQUEST;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kr.co.zerobase.weather.exception.WeatherException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String weather;

    private String icon;

    private double temperature;

    private String text;

    private LocalDate date;

    public static Diary createWithDateWeather(String text, LocalDate date,
        DateWeather dateWeather) {
        if (dateWeather == null) {
            throw new WeatherException(INVALID_REQUEST);
        }

        return Diary.builder()
            .weather(dateWeather.getWeather())
            .icon(dateWeather.getIcon())
            .temperature(dateWeather.getTemperature())
            .text(text)
            .date(date)
            .build();
    }
}

