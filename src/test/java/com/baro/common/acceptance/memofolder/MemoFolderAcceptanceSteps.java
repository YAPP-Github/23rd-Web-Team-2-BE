package com.baro.common.acceptance.memofolder;

import static io.restassured.RestAssured.given;

import com.baro.auth.domain.Token;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class MemoFolderAcceptanceSteps {

    public static ExtractableResponse<Response> 메모_폴더_생성_요청(Token 토큰, SaveMemoFolderRequest 바디) {
        var url = "/memo-folders";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메모_폴더_불러오기_요청(Token 토큰) {
        var url = "/memo-folders";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
