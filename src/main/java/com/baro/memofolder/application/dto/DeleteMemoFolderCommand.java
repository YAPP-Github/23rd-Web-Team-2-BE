package com.baro.memofolder.application.dto;

public record DeleteMemoFolderCommand(
        Long memberId,
        Long memoFolderId,
        boolean deleteAllMemo
) {
}
