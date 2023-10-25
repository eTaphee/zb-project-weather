package kr.co.zerobase.weather.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value()),
    COULD_NOT_GET_WEATHER_DATA(HttpStatus.INTERNAL_SERVER_ERROR.value()),
    INVALID_REQUEST(BAD_REQUEST.value()),
    DIARY_NOT_FOUND(NOT_FOUND.value()),
    INVALID_DATE(BAD_REQUEST.value()),
    INVALID_DATE_END_DATE_IS_BEFORE_START_DATE(BAD_REQUEST.value());

    private final int status;
}
