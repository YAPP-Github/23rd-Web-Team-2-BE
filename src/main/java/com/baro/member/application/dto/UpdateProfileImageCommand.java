package com.baro.member.application.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileImageCommand(
        Long id,
        MultipartFile image
) {
}
