package com.baro.memo.presentation;


import com.baro.auth.domain.AuthMember;
import com.baro.memo.application.MemoService;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.SaveTemporalMemoResult;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping("/memos")
@RestController
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/temporal")
    public ResponseEntity<Void> saveTemporalMemo(AuthMember authMember, @RequestBody SaveTemporalMemoRequest request) {
        SaveTemporalMemoCommand command = new SaveTemporalMemoCommand(authMember.id(), request.content());
        SaveTemporalMemoResult result = memoService.saveTemporalMemo(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
