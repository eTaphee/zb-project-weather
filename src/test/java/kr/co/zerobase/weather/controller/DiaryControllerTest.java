package kr.co.zerobase.weather.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import kr.co.zerobase.weather.dto.DiaryDto;
import kr.co.zerobase.weather.service.DiaryService;
import kr.co.zerobase.weather.util.MockMvcUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("일기 생성 성공")
    void successCreateDiary() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performPost(mockMvc,
            "/create/diary?date=2023-01-01", "일기내용");

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("일기 생성 실패 - 내용 없음")
    void failCreateDiary_text_NotNull() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performPost(mockMvc,
            "/create/diary?date=2023-01-01");

        // then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("일기 생성 실패 - 날짜 없음")
    void failCreateDiary_date_NotNull() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performPost(mockMvc, "/create/diary");

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("일기 수정 성공")
    void successUpdateDiary() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performPut(mockMvc,
            "/update/diary?date=2023-01-01", "일기내용");

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("일기 수정 실패 - 내용 없음")
    void failUpdateDiary_text_NotNull() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performPut(mockMvc,
            "/update/diary?date=2023-01-01");

        // then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("일기 수정 실패 - 날짜 없음")
    void failUpdateDiary_date_NotNull() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performPut(mockMvc, "/update/diary");

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("일기 삭제 성공")
    void successDeleteDiary() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performDelete(mockMvc,
            "/delete/diary?date=2023-01-01");

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("일기 삭제 실패 - 날짜 없음")
    void failDeleteDiary_date_NotNull() throws Exception {
        // given

        // when
        ResultActions resultActions = MockMvcUtil.performDelete(mockMvc, "/delete/diary");

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("일기 조회 성공")
    void successReadDiary() throws Exception {
        // given
        given(diaryService.readDiary(any()))
            .willReturn(Collections.singletonList(
                DiaryDto.builder()
                    .date(LocalDate.now())
                    .icon("icon")
                    .weather("weather")
                    .text("일기내용")
                    .temperature(12.7)
                    .build()
            ));

        // when
        ResultActions resultActions = MockMvcUtil.performGet(mockMvc,
            "/read/diary?date=2023-01-01");

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].date").isNotEmpty())
            .andExpect(jsonPath("$[0].icon").value("icon"))
            .andExpect(jsonPath("$[0].weather").value("weather"))
            .andExpect(jsonPath("$[0].text").value("일기내용"))
            .andExpect(jsonPath("$[0].temperature").value(12.7));
    }

    @Test
    @DisplayName("일기 범위 조회 성공")
    void successReadDiaries() throws Exception {
        // given
        given(diaryService.readDiaries(any(), any()))
            .willReturn(Collections.singletonList(
                DiaryDto.builder()
                    .date(LocalDate.now())
                    .icon("icon")
                    .weather("weather")
                    .text("일기내용")
                    .temperature(12.7)
                    .build()
            ));

        // when
        ResultActions resultActions = MockMvcUtil.performGet(mockMvc,
            "/read/diaries?startDate=2023-01-01&endDate=2023-12-31");

        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].date").isNotEmpty())
            .andExpect(jsonPath("$[0].icon").value("icon"))
            .andExpect(jsonPath("$[0].weather").value("weather"))
            .andExpect(jsonPath("$[0].text").value("일기내용"))
            .andExpect(jsonPath("$[0].temperature").value(12.7));
    }
}