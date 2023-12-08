package com.baro.test;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class TestController {

    @GetMapping("/tests")
    String getTest() {
        return "test";
    }

    @PostMapping("/tests")
    ResponseEntity<Void> createdTest(
            // @RequestBody CreateTestRequest request
    ) {
        Long createdResourceId = 1L;
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdResourceId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/tests/{id}")
    ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        // delete resource
        return ResponseEntity.noContent().build();
    }
}
