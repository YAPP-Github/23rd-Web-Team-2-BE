package com.baro.archive.presentation;

import com.baro.archive.application.ArchiveService;
import com.baro.auth.domain.AuthMember;
import com.baro.memo.application.dto.ArchiveTemporalMemoCommand;
import com.baro.memo.application.dto.ArchiveTemporalMemoResult;
import com.baro.memo.presentation.dto.ArchiveTemporalMemoRequest;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.ArchiveTemplateResult;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.presentation.dto.ArchiveTemplateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping("/archives")
@RestController
public class ArchiveController {

    private final ArchiveService archiveService;

    @PostMapping("/temporal-memos/{temporalMemoId}")
    public ResponseEntity<Void> archiveAsMemo(
            AuthMember authMember,
            @RequestBody ArchiveTemporalMemoRequest request,
            @PathVariable Long temporalMemoId
    ) {
        ArchiveTemporalMemoCommand command = new ArchiveTemporalMemoCommand(authMember.id(), temporalMemoId,
                request.memoFolderId());
        ArchiveTemporalMemoResult result = archiveService.archiveTemporalMemo(command);

        URI location = ServletUriComponentsBuilder.fromPath("/memos")
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/templates/{templateId}")
    ResponseEntity<Void> archiveTemplate(
            AuthMember authMember,
            @RequestBody ArchiveTemplateRequest request,
            @PathVariable("templateId") Long templateId
    ) {
        ArchiveTemplateCommand command = new ArchiveTemplateCommand(authMember.id(), templateId,
                request.memoFolderId());
        ArchiveTemplateResult result = archiveService.archiveTemplate(command);

        URI location = ServletUriComponentsBuilder.fromPath("/archives/templates")
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/templates/{templateId}")
    ResponseEntity<Void> unarchiveTemplate(
            AuthMember authMember,
            @PathVariable Long templateId
    ) {
        UnArchiveTemplateCommand command = new UnArchiveTemplateCommand(authMember.id(), templateId);
        archiveService.unarchiveTemplate(command);
        return ResponseEntity.noContent().build();
    }
}
