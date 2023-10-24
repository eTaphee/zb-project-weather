package kr.co.zerobase.weather.controller;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kr.co.zerobase.weather.dto.DiaryDto;
import kr.co.zerobase.weather.dto.ErrorResponseDto;
import kr.co.zerobase.weather.service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "일기 API")
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@RestController
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청", response = ErrorResponseDto.class)
    })
    @ApiOperation(
        value = "일기 텍스트와 날씨를 이용해서 DB에 일기를 저장합니다.",
        notes = "OpenWeatherMap API 를 사용해서 얻은 날씨 정보를 함께 저장합니다.")
    @PostMapping("/create/diary")
    public void createDiary(
        @RequestParam @DateTimeFormat(iso = DATE)
        @ApiParam(value = "날짜", example = "2023-01-01", required = true) LocalDate date,
        @RequestBody @Valid @NotNull
        @ApiParam(value = "내용", example = "일기 내용", required = true) String text) {
        diaryService.createDiary(date, text);
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    public List<DiaryDto> readDiary(@RequestParam @DateTimeFormat(iso = DATE)
    @ApiParam(value = "날짜", example = "2023-01-01", required = true) LocalDate date) {
        return diaryService.readDiary(date);
    }

    @ApiOperation("선택한 기간중의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    public List<DiaryDto> readDiaries(
        @RequestParam @DateTimeFormat(iso = DATE)
        @ApiParam(value = "조회할 기간의 첫번째날", example = "2023-01-01") LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DATE)
        @ApiParam(value = "조회할 기간의 마지막날", example = "2023-12-31") LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청", response = ErrorResponseDto.class),
        @ApiResponse(code = 404, message = "데이터 없음")
    })
    @ApiOperation(
        value = "해당 날짜의 첫번째 일기 글을 수정합니다.")
    @PutMapping("/update/diary")
    public void updateDiary(
        @RequestParam @DateTimeFormat(iso = DATE)
        @ApiParam(value = "날짜", example = "2023-01-01", required = true) LocalDate date,
        @RequestBody @Valid @NotNull
        @ApiParam(value = "내용", example = "일기 내용", required = true) String text) {
        diaryService.updateDiary(date, text);
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청", response = ErrorResponseDto.class)
    })
    @ApiOperation(
        value = "입력 날짜의 모든 일기 데이터를 삭제합니다.")
    @DeleteMapping("/delete/diary")
    public void deleteDiary(
        @RequestParam
        @DateTimeFormat(iso = DATE)
        @ApiParam(value = "날짜", example = "2023-01-01", required = true) LocalDate date) {
        diaryService.deleteDiary(date);
    }
}
