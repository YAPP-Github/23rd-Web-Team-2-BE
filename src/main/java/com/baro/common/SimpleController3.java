package com.baro.common;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test3")
public class SimpleController3 {
    /**
     * enum으로 미리 응답값을 정의해놓고, 이를 조립해서 ResponseEntity를 반환해주는 ApiResponse 객체 사용
     */

    @GetMapping("/ok")
    public ResponseEntity<?> ok() {
        return ApiReponse.success(Responses.SUCCESS_DELETE_MEMO);
        // 200
    }

    @GetMapping("/nocontent")
    public ResponseEntity<?> noContent() {
        return ApiReponse.success(Responses.SUCCESS_NO_CONTENT);
        // 204
    }

    @GetMapping("/created")
    public ResponseEntity<?> created() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", "/created/1");
        return ApiReponse.successWithHeader(Responses.SUCCESS_POST_MEMO, headers);
    }

    @GetMapping("/accepted")
    public ResponseEntity<?> accepted() {
        return ApiReponse.success(Responses.SUCCESS_ACCEPTED);
        // 202
    }

    public class ApiReponse {

        public static ResponseEntity<?> success(Responses response) {
            return ResponseEntity.status(response.getStatus()).build();
        }

        public static ResponseEntity<?> successWithHeader(Responses response, HttpHeaders headers) {
            return ResponseEntity.status(response.getStatus()).headers(headers).body(response.getMessage());
        }
    }

    public enum Responses {

        SUCCESS_DELETE_MEMO(HttpStatus.OK, "끄적이는 메모 삭제 성공"),
        SUCCESS_POST_MEMO(HttpStatus.CREATED, "끄적이는 메모 업로드 성공"),
        SUCCESS_NO_CONTENT(HttpStatus.NO_CONTENT, "content 없음"),
        SUCCESS_ACCEPTED(HttpStatus.ACCEPTED, "accepted"),

        /**
         * header에 내용을 담는 response -> noContent, created
         * body에 내용을 담는 response -> ok, ...
         * 흠
         */

        ;

        private HttpStatus status;
        private String message;

        Responses(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }


}
