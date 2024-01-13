package com.baro.common;

import static com.baro.common.acceptance.auth.OAuthAcceptanceSteps.로그인_요청;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import com.baro.auth.application.oauth.OAuthInfoProvider;
import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.domain.Token;
import com.baro.common.data.JpaDataCleaner;
import com.baro.member.domain.MemberRepository;
import com.baro.memofolder.domain.MemoFolderRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class RestApiTest {

    public static final String DEFAULT_REST_DOCS_PATH = "{class_name}/{method_name}";
    public static RequestSpecification requestSpec;
    @Autowired
    protected JpaDataCleaner dataCleaner;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected MemoFolderRepository memoFolderRepository;
    @SpyBean
    OAuthInfoProvider oAuthInfoProvider;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(RestDocumentationContextProvider contextProvider) {
        RestAssured.port = port;
        requestSpec = new RequestSpecBuilder()
                .setPort(port)
                .setContentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8))
                .addFilter(documentationConfiguration(contextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                ).build();
    }

    @AfterEach
    void cleanUp() {
        dataCleaner.cleanUp();
    }

    protected Token 로그인(OAuthMemberInfo memberInfo) {
        OAuth_서버로부터_멤버_정보를_불러온다(memberInfo);
        return 로그인_요청().as(Token.class);
    }

    protected void OAuth_서버로부터_멤버_정보를_불러온다(OAuthMemberInfo memberInfo) {
        doReturn(memberInfo).when(oAuthInfoProvider).getMemberInfo(anyString(), anyString());
    }
}
