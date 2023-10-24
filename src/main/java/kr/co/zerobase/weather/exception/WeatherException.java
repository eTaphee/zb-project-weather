package kr.co.zerobase.weather.exception;

import kr.co.zerobase.weather.type.ErrorCode;
import lombok.Getter;

@Getter
public class WeatherException extends RuntimeException {

    private final int status;
    private final ErrorCode errorCode;

    public WeatherException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode;
    }
}
