package com.baro.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.AuthMember;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthenticationArgumentResolverTest {

    @MockBean
    TokenTranslator tokenTranslator;

    @Autowired
    AuthenticationArgumentResolver authenticationArgumentResolver;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 매개변수에_AuthMember가_존재하는경우_Argument_resolver를_거친다() {
        // given
        String testToken = "token";
        Long authMemberId = 1L;
        when(tokenTranslator.decodeAccessToken(testToken)).thenReturn(authMemberId);

        // when
        Response response = RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .header("Authorization", testToken)
                .get("/test/auth");

        // then
        String result = response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
        assertThat(result).isEqualTo(authMemberId.toString());
    }
}

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/auth")
    private Long authUserMethod(AuthMember authMember) {
        return authMember.id();
    }
}
