package com.baro.memo.presentation;


import com.baro.auth.domain.AuthMember;
import com.baro.memo.application.TemporalMemoService;
import com.baro.memo.application.dto.ApplyCorrectionCommand;
import com.baro.memo.application.dto.ArchiveTemporalMemoCommand;
import com.baro.memo.application.dto.ArchiveTemporalMemoResult;
import com.baro.memo.application.dto.DeleteTemporalMemoCommand;
import com.baro.memo.application.dto.FindTemporalMemoHistoriesQuery;
import com.baro.memo.application.dto.FindTemporalMemoHistoriesResult;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.SaveTemporalMemoResult;
import com.baro.memo.application.dto.UnarchiveTemporalMemoCommand;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.presentation.dto.ApplyCorrectionRequest;
import com.baro.memo.presentation.dto.ArchiveTemporalMemoRequest;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import com.baro.memo.presentation.dto.UpdateTemporalMemoRequest;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping("/temporal-memos")
@RestController
public class TemporalMemoController {

    private final TemporalMemoService temporalMemoService;

    @PostMapping
    public ResponseEntity<Void> saveTemporalMemo(AuthMember authMember, @RequestBody SaveTemporalMemoRequest request) {
        SaveTemporalMemoCommand command = new SaveTemporalMemoCommand(authMember.id(), request.content());
        SaveTemporalMemoResult result = temporalMemoService.saveTemporalMemo(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{temporalMemoId}")
    public ResponseEntity<Void> updateTemporalMemo(
            AuthMember authMember,
            @RequestBody UpdateTemporalMemoRequest request,
            @PathVariable Long temporalMemoId
    ) {
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(authMember.id(), temporalMemoId,
                request.content());
        temporalMemoService.updateTemporalMemo(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{temporalMemoId}")
    public ResponseEntity<Void> deleteTemporalMemo(
            AuthMember authMember,
            @PathVariable Long temporalMemoId
    ) {
        DeleteTemporalMemoCommand command = new DeleteTemporalMemoCommand(authMember.id(), temporalMemoId);
        temporalMemoService.deleteTemporalMemo(command);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{temporalMemoId}/correction")
    public ResponseEntity<Void> applyCorrection(
            AuthMember authMember,
            @RequestBody ApplyCorrectionRequest request,
            @PathVariable Long temporalMemoId
    ) {
        ApplyCorrectionCommand command = new ApplyCorrectionCommand(authMember.id(), temporalMemoId,
                request.correctionContent(), request.styledCorrectionContent());
        temporalMemoService.applyCorrection(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FindTemporalMemoHistoriesResult>> findTemporalMemos(
            AuthMember authMember,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        FindTemporalMemoHistoriesQuery query = new FindTemporalMemoHistoriesQuery(authMember.id(), startDate, endDate);
        List<FindTemporalMemoHistoriesResult> results = temporalMemoService.findTemporalMemos(query);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{temporalMemoId}/archive")
    public ResponseEntity<Void> archiveAsMemo(
            AuthMember authMember,
            @RequestBody ArchiveTemporalMemoRequest request,
            @PathVariable Long temporalMemoId
    ) {
        ArchiveTemporalMemoCommand command = new ArchiveTemporalMemoCommand(authMember.id(), temporalMemoId,
                request.memoFolderId());
        ArchiveTemporalMemoResult result = temporalMemoService.archiveTemporalMemo(command);

        URI location = ServletUriComponentsBuilder.fromPath("/memos")
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{temporalMemoId}/archive")
    public ResponseEntity<Void> deleteArchive(
            AuthMember authMember,
            @PathVariable Long temporalMemoId
    ) {
        UnarchiveTemporalMemoCommand command = new UnarchiveTemporalMemoCommand(authMember.id(), temporalMemoId);
        temporalMemoService.deleteArchive(command);
        return ResponseEntity.noContent().build();
    }
}
