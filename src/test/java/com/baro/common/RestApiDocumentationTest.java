package com.baro.common;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @LocalServerPort
    private int port;

    protected RequestSpecification requestSpec;

    @BeforeEach
    void setUp(final RestDocumentationContextProvider contextProvider) {
        RestAssured.port = port;
        requestSpec = new RequestSpecBuilder()
                .setPort(port)
                .addFilter(documentationConfiguration(contextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                ).build();
    }
}
