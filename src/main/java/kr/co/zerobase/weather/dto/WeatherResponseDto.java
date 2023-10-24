package kr.co.zerobase.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;

@Getter
public final class WeatherResponseDto {

    private String weather;

    private String icon;

    private Double temperature;

    @SuppressWarnings("unchecked")
    @JsonProperty("weather")
    private void unpackNestedWeather(Map<String, Object>[] weather) {
        this.weather = weather[0].get("main").toString();
        this.icon = weather[0].get("icon").toString();
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("main")
    private void unpackNestedMain(Map<String, Object> main) {
        this.temperature = (Double) main.get("temp");
    }
}
