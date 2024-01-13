package com.baro.common;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class RestApiDocumentationTest {

    protected static final String DEFAULT_REST_DOCS_PATH = "{class_name}/{method_name}";
    protected RequestSpecification requestSpec;
    @Autowired
    protected JpaDataCleaner dataCleaner;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected MemoFolderRepository memoFolderRepository;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(final RestDocumentationContextProvider contextProvider) {
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
}
