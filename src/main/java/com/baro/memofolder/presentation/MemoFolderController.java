package com.baro.memofolder.presentation;

import com.baro.auth.domain.AuthMember;
import com.baro.memofolder.application.MemoFolderService;
import com.baro.memofolder.application.dto.GetMemoFolderResult;
import com.baro.memofolder.application.dto.RenameMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderResult;
import com.baro.memofolder.presentation.dto.RenameMemoFolderRequest;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping("/memo-folders")
@RestController
public class MemoFolderController {

    private final MemoFolderService memoFolderService;

    @PostMapping
    public ResponseEntity<Void> saveMemoFolder(AuthMember authMember, @RequestBody SaveMemoFolderRequest folderName) {
        SaveMemoFolderCommand command = new SaveMemoFolderCommand(authMember.id(), folderName.folderName());
        SaveMemoFolderResult result = memoFolderService.saveMemoFolder(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<GetMemoFolderResult>> getMemoFolder(AuthMember authMember) {
        List<GetMemoFolderResult> result = memoFolderService.getMemoFolder(authMember.id());
        return ResponseEntity.ok(result);
    }

    @PatchMapping
    public ResponseEntity<Void> renameMemoFolder(AuthMember authMember, @RequestBody RenameMemoFolderRequest request) {
        RenameMemoFolderCommand command = new RenameMemoFolderCommand(authMember.id(), request.memoFolderId(),
                request.folderName());
        memoFolderService.renameMemoFolder(command);
        return ResponseEntity.noContent().build();
    }
}
