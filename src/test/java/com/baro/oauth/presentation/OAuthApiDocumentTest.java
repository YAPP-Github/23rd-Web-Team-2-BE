package com.baro.oauth.presentation;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import com.baro.common.RestApiDocumentationTest;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class OAuthApiDocumentTest extends RestApiDocumentationTest {

    @Test
    void oauth_sign_in_url() {
        // given
        var url = "/oauth/{oAuthService}";

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oAuthService").description("OAuth 로그인 소셜 타입")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("로그인 url")
                        ))
                ).redirects().follow(false)
                .when().get(url, "kakao")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
        String location = new String(response.header("Location").getBytes(StandardCharsets.UTF_8));
        assertThat(location).isNotNull();
    }
}
