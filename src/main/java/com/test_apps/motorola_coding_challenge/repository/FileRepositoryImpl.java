package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {
    private final StorageService storageService;

    @Override
    public List<String> getAllNames() {
        log.info("Getting all files by name");
        return List.of();
    }

    @Override
    public Resource get(String fileName) {
        log.info("Getting file with name: {}", fileName);
        return null;
    }

    @Override
    public String save(MultipartFile file) {
        final String fileName = file.getName();
        log.info("Saving file with name: {}", fileName);
        try {
            Files.copy(file.getInputStream(), storageService.buildFilePath(fileName), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            log.info("Saving failed for file {} with message: {}", fileName, e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(String fileName) {
        log.info("Deleting file with name: {}", fileName);
    }
}
