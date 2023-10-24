package kr.co.zerobase.weather.domain;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import kr.co.zerobase.weather.dto.WeatherResponseDto;
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
public class DateWeather extends BaseEntity {

    @Id
    private LocalDate date;

    private String weather;

    private String icon;

    private double temperature;

    public static DateWeather from(WeatherResponseDto dto) {
        return DateWeather.builder()
            .date(LocalDate.now())
            .weather(dto.getWeather())
            .icon(dto.getIcon())
            .temperature(dto.getTemperature())
            .build();
    }
}
