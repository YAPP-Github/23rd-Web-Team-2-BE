package com.baro.memofolder.presentation;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
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
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class MemoFolderApiDocumentTest extends RestApiDocumentationTest {

    private static final String SET_UP_ACCESS_TOKEN = "accessToken";

    @MockBean
    TokenTranslator tokenTranslator;

    @Test
    void create_memo_folder() {
        // given
        var url = "/memo-folders";
        var request = new SaveMemoFolderRequest("회사생활👔");
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        setTokenDecrypt(savedMember);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 폴더 경로")
                        ),
                        requestFields(
                                fieldWithPath("folderName").description("폴더 이름")
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
    void create_memo_folder_duplication() {
        // given
        var url = "/memo-folders";
        String duplicationName = "회사생활👔";
        var request = new SaveMemoFolderRequest(duplicationName);
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        setTokenDecrypt(savedMember);
        memoFolderRepository.save(MemoFolder.of(savedMember, duplicationName));

        // when
        var response = given(requestSpec).log().all()
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
                ).header(HttpHeaders.AUTHORIZATION, SET_UP_ACCESS_TOKEN).body(request)
                .when().post(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_memo_folder_not_exist_member() {
        // given
        var url = "/memo-folders";
        var request = new SaveMemoFolderRequest("회사생활👔");
        setTokenDecryptAsNotExistMember();

        // when
        var response = given(requestSpec).log().all()
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
                ).header(HttpHeaders.AUTHORIZATION, SET_UP_ACCESS_TOKEN).body(request)
                .when().post(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    void setTokenDecrypt(Member savedMember) {
        given(tokenTranslator.decode(SET_UP_ACCESS_TOKEN)).willReturn(savedMember.getId());
    }

    void setTokenDecryptAsNotExistMember() {
        given(tokenTranslator.decode(SET_UP_ACCESS_TOKEN)).willReturn(9999L);
    }
}
