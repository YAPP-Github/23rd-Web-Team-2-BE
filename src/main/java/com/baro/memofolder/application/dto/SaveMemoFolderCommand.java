package com.baro.memofolder.application.dto;

public record SaveMemoFolderCommand(
        Long memberId,
        String name
) {
}
