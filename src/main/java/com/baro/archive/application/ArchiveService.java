package com.baro.archive.application;

import com.baro.archive.domain.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
}
