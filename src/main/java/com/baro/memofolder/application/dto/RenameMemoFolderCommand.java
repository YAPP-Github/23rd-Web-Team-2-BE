package com.baro.memofolder.application.dto;

public record RenameMemoFolderCommand(
        Long memberId,
        Long memoFolderId,
        String folderName
) {
}
