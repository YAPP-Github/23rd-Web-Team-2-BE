package com.baro.archive.presentation;

import com.baro.archive.application.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/archives")
@RestController
public class ArchiveController {

    private final ArchiveService archiveService;
}
