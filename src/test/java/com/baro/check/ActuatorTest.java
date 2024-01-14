package com.baro.check;

import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_특정_필드값을_검증한다;
import static com.baro.common.acceptance.check.ActuatorAcceptanceSteps.액추에이터_헬스체크_요청;

import com.baro.common.RestApiTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ActuatorTest extends RestApiTest {

    @Test
    void 헬스_체크() {
        // when
        var 응답 = 액추에이터_헬스체크_요청();

        // then
        응답값을_검증한다(응답, 성공);
        응답의_특정_필드값을_검증한다(응답, "status", "UP");
    }
}
