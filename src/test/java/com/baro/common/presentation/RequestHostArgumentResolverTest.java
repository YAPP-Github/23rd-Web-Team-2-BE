package com.baro.common.presentation;

import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_특정_필드값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.presentation.RequestHostArgumentResolverAcceptanceSteps.Origin_헤더가_없는_요청;
import static com.baro.common.presentation.RequestHostArgumentResolverAcceptanceSteps.Origin_헤더를_포함한_요청;
import static io.restassured.RestAssured.given;

import com.baro.common.RestApiTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RequestHostArgumentResolverTest extends RestApiTest {

    @Autowired
    RequestHostArgumentResolver requestHostArgumentResolver;

    @Test
    void 매개변수에_RequestHost가_존재하는경우_Argument_resolver를_거친다() {
        // given
        var 요청_도메인 = "http://localhost:3000";

        // when
        var 응답 = Origin_헤더를_포함한_요청(요청_도메인);

        // then
        응답값을_검증한다(응답, 성공);
        응답의_특정_필드값을_검증한다(응답, "host", 요청_도메인);
    }

    @Test
    void RequestHost가_존재하지_않으면_예외_발생() {
        // given
        var 요청_도메인 = "http://localhost:3000";

        // when
        var 응답 = Origin_헤더가_없는_요청(요청_도메인);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }
}

@RestController
@RequestMapping("/test")
class RequestHostTestController {

    @GetMapping("/host")
    private ResponseEntity<TestResponse> authUserMethod(@RequestHost String host) {
        return ResponseEntity.ok().body(new TestResponse(host));
    }

    record TestResponse(String host) {
    }
}

class RequestHostArgumentResolverAcceptanceSteps {

    public static ExtractableResponse<Response> Origin_헤더를_포함한_요청(String 요청_도메인) {
        var url = "/test/host";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ORIGIN, 요청_도메인)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> Origin_헤더가_없는_요청(String 요청_도메인) {
        var url = "/test/host";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
