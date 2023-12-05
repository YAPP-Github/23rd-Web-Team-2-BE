package com.baro.check;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.baro.common.RestApiDocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ActuatorTest extends RestApiDocumentationTest {

    @Test
    void health_check() {
        // given
        var url = "/actuator/health";

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH, responseFields(
                        fieldWithPath("status").description("상태")
                        )))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        var message = response.body().jsonPath().getString("status");
        assertThat(message).isEqualTo("UP");
    }
}
