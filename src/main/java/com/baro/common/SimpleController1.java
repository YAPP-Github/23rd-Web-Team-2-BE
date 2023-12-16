package com.baro.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/test1")
public class SimpleController1 {
    /**
     * 그냥 Controller에서 직접 만드는 방식
     */

    @GetMapping("/ok")
    public ResponseEntity<?> ok() {
        return ResponseEntity.ok().build();
        // 200
    }

    @GetMapping("/nocontent")
    public ResponseEntity<?> noContent() {
        return ResponseEntity.noContent().build();
        // 204
    }

    @GetMapping("/created")
    public ResponseEntity<?> created() {
        return ResponseEntity.created(URI.create("/test/1")).build();
        // 201
    }

    @GetMapping("/accepted")
    public ResponseEntity<?> accepted() {
        return ResponseEntity.accepted().build();
        // 202
    }

    @GetMapping("/badrequest")
    public ResponseEntity<?> badRequest() {
        return ResponseEntity.badRequest().build();
        // 400
    }

    @GetMapping("/notfound")
    public ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
        // 404
    }

    @GetMapping("/payload")
    public ResponseEntity<?> payloadTooLarge() {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        // 413
    }
}
