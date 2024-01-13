package com.baro.auth.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.아현;
import static com.baro.auth.presentation.ArgumentResolverAcceptanceSteps.Authorization_헤더를_포함한_요청;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_특정_필드값을_검증한다;
import static io.restassured.RestAssured.given;

import com.baro.auth.domain.AuthMember;
import com.baro.auth.domain.Token;
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
class AuthenticationArgumentResolverTest extends RestApiTest {

    @Autowired
    AuthenticationArgumentResolver authenticationArgumentResolver;

    @Test
    void 매개변수에_AuthMember가_존재하는경우_Argument_resolver를_거친다() {
        // given
        var 토큰 = 로그인(아현());

        // when
        var 응답 = Authorization_헤더를_포함한_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
        응답의_특정_필드값을_검증한다(응답, "id", "1");
    }
}

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/auth")
    private ResponseEntity<TestResponse> authUserMethod(AuthMember authMember) {
        return ResponseEntity.ok().body(new TestResponse(authMember.id()));
    }

    record TestResponse(Long id) {
    }
}

class ArgumentResolverAcceptanceSteps {

    public static ExtractableResponse<Response> Authorization_헤더를_포함한_요청(Token 토큰) {
        var url = "/test/auth";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
