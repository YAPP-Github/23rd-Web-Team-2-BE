package com.baro.memo.presentation;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.baro.auth.application.TokenTranslator;
import com.baro.common.RestApiDocumentationTest;
import com.baro.member.domain.Member;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import io.restassured.RestAssured;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
public class MemoApiTest extends RestApiDocumentationTest {

    private static final String SET_UP_ACCESS_TOKEN = "accessToken";

    @MockBean
    TokenTranslator tokenTranslator;

    @Test
    void create_temporal_memo() {
        // given
        var url = "/memos/temporal";
        var request = new SaveTemporalMemoRequest("끄적이는 메모 컨텐츠");
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로")); // TODO: 인수테스트 steps로 변경
        setTokenDecrypt(savedMember);

        // when
        var response = RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 끄적이는 메모 경로")
                        ),
                        requestFields(
                                fieldWithPath("content").description("끄적이는 메모 내용")
                        ))
                ).header(HttpHeaders.AUTHORIZATION, SET_UP_ACCESS_TOKEN).body(request)
                .when().post(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = new String(response.header("Location").getBytes(StandardCharsets.UTF_8));
        assertThat(location).isNotNull();
    }

    @Test
    void create_temporal_memo_over_max_size_content() {
        // given
        var url = "/memos/temporal";
        var request = new SaveTemporalMemoRequest("끄적이는 메모 컨텐츠".repeat(500));
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로")); // TODO: 인수테스트 steps로 변경
        setTokenDecrypt(savedMember);

        // when
        var response = RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                ).header(HttpHeaders.AUTHORIZATION, SET_UP_ACCESS_TOKEN).body(request)
                .when().post(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    void setTokenDecrypt(Member savedMember) {
        given(tokenTranslator.decodeAccessToken(SET_UP_ACCESS_TOKEN)).willReturn(savedMember.getId());
    }
}
