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

import java.net.URI;

@RestController
@RequestMapping("/test2")
public class SimpleController2 {
    /**
     * 다양한 생성자를 가진 ResponseEntity를 사용하는 방식
     * 참고 : https://velog.io/@qotndus43/%EC%8A%A4%ED%94%84%EB%A7%81-API-%EA%B3%B5%ED%86%B5-%EC%9D%91%EB%8B%B5-%ED%8F%AC%EB%A7%B7-%EA%B0%9C%EB%B0%9C%ED%95%98%EA%B8%B0
     */

    @GetMapping("/ok")
    public ResponseEntity<?> ok() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
        // 200
    }

    @GetMapping("/nocontent")
    public ResponseEntity<?> noContent() {
        return new ResponseEntity<>("no content", HttpStatus.NO_CONTENT);
        // 204
    }

    @GetMapping("/created")
    public ResponseEntity<?> created() {
        MultiValueMap<String, String> map = new HttpHeaders();
        map.set("location", "/created/1");
        return new ResponseEntity<>("created", map, HttpStatus.CREATED);
        // 201
    }

    @GetMapping("/accepted")
    public ResponseEntity<?> accepted() {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
        // 202
    }

    @GetMapping("/badrequest")
    public ResponseEntity<?> badRequest() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // 400
    }

    @GetMapping("/notfound")
    public ResponseEntity<?> notFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // 404
    }

    @GetMapping("/payload")
    public ResponseEntity<?> payloadTooLarge() {
        return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        // 413
    }

    public class ResponseEntity<T> extends HttpEntity<T> {

        private final HttpStatus status;

        /**
         * 생성자 vs 빌더
         */

        public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
            super(body, headers);
            Assert.notNull(status, "HttpStatus must not be null");
            this.status = status;
        }

        public ResponseEntity(HttpStatus status) {
            this(null, null, status);
        }

        public ResponseEntity(@Nullable T body, HttpStatus status) {
            this(body, null, status);
        }
    }
}
