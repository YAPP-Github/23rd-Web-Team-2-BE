package com.baro.test;

import com.baro.common.exception.ApiResponseType;
import com.baro.common.response.ApiResponse;
import com.baro.common.response.ApiResponseBody;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class TestController2 {

    @GetMapping("/tests-2")
    ApiResponse<ApiResponseBody<ApiResponseType>> getTestB() {
        return ApiResponse.ok(TestApiResponseType.GET_TEST);
    }

    @GetMapping("/tests-23")
    ApiResponse<ApiResponseBody<String>> getTestC() {
        return ApiResponse.ok(TestApiResponseType.GET_TEST, "test data");
    }

    @PostMapping("/tests-2")
    ApiResponse<ApiResponseBody<ApiResponseType>> createdTest(
            // @RequestBody CreateTestRequest request
    ) {
        Long createdResourceId = 1L;
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdResourceId)
                .toUri();
        return ApiResponse.created(TestApiResponseType.POST_TEST, location.toString());
    }

    @DeleteMapping("/tests-2/{id}")
    ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        // delete resource
        return ApiResponse.noContent().build();
    }
}
