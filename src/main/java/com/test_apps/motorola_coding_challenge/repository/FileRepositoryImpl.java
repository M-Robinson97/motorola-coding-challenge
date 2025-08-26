package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.test_apps.motorola_coding_challenge.utility.ExceptionMessages.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {
    private final StorageService storageService;

    @Override
    public List<String> getAllFileNames() {
        try {
            final Path rootPath = storageService.getPathFromRoot();
            log.info("Getting file names from: {}", rootPath);
            final Stream<Path> allFilePaths = storageService.getAllPaths(rootPath);

            return allFilePaths
                    .map(Path::toString)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(GET_ALL_FILES_FAILS, e);
        }
    }

    @Override
    public Resource get(String fileName) {
        try {
            Optional.ofNullable(fileName)
                    .filter(name -> !name.isEmpty())
                    .orElseThrow(() -> new IllegalArgumentException(FILE_NAME_REQUIRED));
            log.info("Getting file with name: {}", fileName);

            final Path filePath = storageService.createFilePath(fileName);
            final URI filePathUri = storageService.createUri(filePath);
            final Resource resource = storageService.createResource(filePathUri);

            if (!resource.exists() || !resource.isReadable()) {
                throw new NoSuchFileException(FILE_NOT_FOUND);
            }
            return resource;
        } catch (Exception e) {
            throw new RuntimeException(GET_FILE_FAILS, e);
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            Optional.of(file)
                    .filter(f -> !f.getName().isEmpty())
                    .orElseThrow(() -> new IllegalArgumentException(FILE_NAME_REQUIRED));

            final String fileName = file.getName();

            log.info("Saving file with name: {}", fileName);

            final Path filePath = storageService.createFilePath(fileName);

            final InputStream inputStream = file.getInputStream();

            storageService.copyFile(inputStream, filePath);

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException(SAVE_FILE_FAILS, e);
        }
    }

    @Override
    public boolean delete(String fileName) {
        try {
            Optional.ofNullable(fileName)
                    .filter(name -> !name.isEmpty())
                    .orElseThrow(() -> new IllegalArgumentException(FILE_NAME_REQUIRED));
            log.info("Deleting file with name: {}", fileName);

            final Path filePath = storageService.createFilePath(fileName);
            storageService.deleteFile(filePath);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(DELETE_FILE_FAILS, e);
        }
    }
}
