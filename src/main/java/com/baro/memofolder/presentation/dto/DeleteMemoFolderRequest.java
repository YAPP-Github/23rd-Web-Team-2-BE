package com.baro.memofolder.presentation.dto;

public record DeleteMemoFolderRequest(
        Long memoFolderId,
        boolean deleteAllMemo
) {
}
