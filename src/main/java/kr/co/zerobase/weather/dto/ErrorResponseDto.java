package kr.co.zerobase.weather.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.zerobase.weather.type.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {

    @ApiModelProperty(value = "응답 코드", example = "400", required = true)
    private final int status;

    @ApiModelProperty(value = "에러 코드", example = "INVALID_REQUEST", required = true)
    private final ErrorCode errorCode;
}
