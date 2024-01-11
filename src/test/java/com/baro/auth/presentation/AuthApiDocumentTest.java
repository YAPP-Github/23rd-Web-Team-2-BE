package com.baro.auth.presentation;

import com.baro.auth.application.AuthService;
import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.common.RestApiDocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

public class AuthApiDocumentTest extends RestApiDocumentationTest {

    @Autowired
    AuthService authService;

    @Test
    void reissue() {
        // given
        var url = "/auth/reissue";
        SignInDto dto = new SignInDto("name", "email", "1", "kakao");
        Token token = authService.signIn(dto);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
