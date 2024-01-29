package com.baro.archive.presentation;

import com.baro.archive.application.ArchiveService;
import com.baro.archive.application.dto.ArchiveUnitResult;
import com.baro.archive.application.dto.GetArchiveQuery;
import com.baro.archive.domain.ArchiveTab;
import com.baro.archive.presentation.dto.GetArchiveRequest;
import com.baro.auth.domain.AuthMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/archives")
@RestController
public class ArchiveController {

    private final ArchiveService archiveService;

    @GetMapping
    public ResponseEntity<List<ArchiveUnitResult>> getArchive(AuthMember member,
                                                              @RequestBody GetArchiveRequest request) {
        GetArchiveQuery query = new GetArchiveQuery(member.id(), request.folderId(),
                ArchiveTab.from(request.tabName()));
        return ResponseEntity.ok(archiveService.getArchive(query));
    }
}
