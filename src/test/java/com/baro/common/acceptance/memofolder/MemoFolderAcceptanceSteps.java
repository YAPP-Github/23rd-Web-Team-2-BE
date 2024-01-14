package com.baro.common.acceptance.memofolder;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.baro.auth.domain.Token;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class MemoFolderAcceptanceSteps {


    public static final SaveMemoFolderRequest 폴더_이름_바디 = new SaveMemoFolderRequest("회사생활👔");
    public static final SaveMemoFolderRequest 폴더_이름_길이_초과_바디 = new SaveMemoFolderRequest("회사생활은재미없겠지만해야겠지👔👔👔");

    public static ExtractableResponse<Response> 메모_폴더_생성_요청(Token 토큰, SaveMemoFolderRequest 바디) {
        var url = "/memo-folders";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("folderName").description("폴더 이름")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 폴더 경로")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static Long 메모_폴더를_생성_하고_ID를_반환한다(Token 토큰, SaveMemoFolderRequest 바디) {
        String location = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/memo-folders")
                .then().log().all()
                .extract()
                .response().header(HttpHeaders.LOCATION);

        String[] split = location.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

    public static ExtractableResponse<Response> 잘못된_메모_폴더_생성_요청(Token 토큰, SaveMemoFolderRequest 바디) {
        var url = "/memo-folders";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("folderName").description("폴더 이름")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메모_폴더_불러오기_요청(Token 토큰) {
        var url = "/memo-folders";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("폴더 id"),
                                fieldWithPath("[].name").description("폴더 이름")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
