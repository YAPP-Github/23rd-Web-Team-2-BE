package com.baro.memofolder.presentation.dto;

public record RenameMemoFolderRequest(
        Long memoFolderId,
        String folderName
) {
}
