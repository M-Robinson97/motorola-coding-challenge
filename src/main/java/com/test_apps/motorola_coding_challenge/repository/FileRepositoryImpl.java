package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

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
        try {
            Optional.ofNullable(fileName)
                    .filter(name -> !name.isEmpty())
                    .orElseThrow(() -> new IllegalArgumentException("Error: file name required"));
            log.info("Getting file with name: {}", fileName);

            final URI filePathUri = storageService.buildFilePathUri(fileName);

            Resource resource = new UrlResource(filePathUri);

            if(!resource.exists() || !resource.isReadable()) {
                throw new NoSuchFileException("File not found or not readable");
            }
            return resource;
        } catch (Exception e) {
            log.info("Get failed with message: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            Optional.of(file)
                    .filter(f -> !f.getName().isEmpty())
                    .orElseThrow(() -> new IllegalArgumentException("Error: file name required"));

            final String fileName = file.getName();

            log.info("Saving file with name: {}", fileName);

            Files.copy(file.getInputStream(), storageService.buildFilePath(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            log.info("Saving failed with message: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(String fileName) {
        log.info("Deleting file with name: {}", fileName);
    }
}
