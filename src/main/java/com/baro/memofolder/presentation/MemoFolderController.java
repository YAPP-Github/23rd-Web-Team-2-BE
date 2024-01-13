package com.baro.memofolder.presentation;

import com.baro.auth.domain.AuthMember;
import com.baro.memofolder.application.MemoFolderService;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderResult;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
