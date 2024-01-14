package com.baro.memo.presentation;


import com.baro.auth.domain.AuthMember;
import com.baro.memo.application.MemoService;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.SaveTemporalMemoResult;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import com.baro.memo.presentation.dto.UpdateTemporalMemoRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PatchMapping("/temporal/{temporalMemoId}")
    public ResponseEntity<Void> updateTemporalMemo(
            AuthMember authMember,
            @RequestBody UpdateTemporalMemoRequest request,
            @PathVariable Long temporalMemoId
    ) {
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(authMember.id(), temporalMemoId,
                request.content());
        memoService.updateTemporalMemo(command);
        return ResponseEntity.noContent().build();
    }
}
