package com.baro.archive.presentation;

import com.baro.archive.application.ArchiveService;
import com.baro.archive.application.dto.ArchiveUnitResult;
import com.baro.archive.application.dto.GetArchiveQuery;
import com.baro.archive.application.dto.ModifyArchiveCommand;
import com.baro.archive.domain.ArchiveTab;
import com.baro.archive.presentation.dto.ModifyArchiveRequest;
import com.baro.auth.domain.AuthMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/archives")
@RestController
public class ArchiveController {

    private final ArchiveService archiveService;

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<ArchiveUnitResult>> getArchive(AuthMember member,
                                                              @PathVariable("folderId") Long folderId,
                                                              @RequestParam String tabName) {
        GetArchiveQuery query = new GetArchiveQuery(member.id(), folderId, ArchiveTab.from(tabName));
        return ResponseEntity.ok(archiveService.getArchive(query));
    }

    @PatchMapping("/{archiveId}")
    public ResponseEntity<List<ArchiveUnitResult>> modifyArchive(AuthMember member,
                                                                 @PathVariable Long archiveId,
                                                                 @RequestBody ModifyArchiveRequest request) {
        ModifyArchiveCommand command = new ModifyArchiveCommand(member.id(), archiveId, request.content());
        archiveService.modifyArchive(command);
        return ResponseEntity.noContent().build();
    }
}
