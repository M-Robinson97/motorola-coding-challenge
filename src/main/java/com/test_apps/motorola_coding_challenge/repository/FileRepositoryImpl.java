package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.model.FileListDto;
import com.test_apps.motorola_coding_challenge.repository.exception.FileNotFoundException;
import com.test_apps.motorola_coding_challenge.repository.exception.FileSystemException;
import com.test_apps.motorola_coding_challenge.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import static com.test_apps.motorola_coding_challenge.repository.exception.ExceptionMessages.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {
    private final StorageService storageService;

    @Override
    public FileListDto getAllFileNames() {
            final Path rootPath = storageService.getPathFromRoot();
            log.info("Getting file names from: {}", rootPath);
        try {
            List<String> allFileNames =  storageService.getAllPaths(rootPath);
            return new FileListDto(allFileNames);
        } catch (Exception e) {
            throw new FileSystemException(GET_ALL_FILES_FAILS, e);
        }
    }

    @Override
    public Resource get(String fileName) {
        log.info("Getting file with name: {}", fileName);

        final Path filePath = storageService.createFilePath(fileName);
        final URI filePathUri = storageService.createUri(filePath);
        Resource resource;
        try {
            resource = storageService.createResource(filePathUri);
        } catch (Exception e) {
            throw new FileSystemException(GET_FILE_FAILS, e);
        }
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_ERROR);
        }
        return resource;
    }

    @Override
    public String save(MultipartFile file, String fileName) {
        log.info("Saving file with name: {}", fileName);

        final Path filePath = storageService.createFilePath(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            storageService.copyFile(inputStream, filePath);
            return fileName;
        } catch (Exception e) {
            throw new FileSystemException(SAVE_FILE_FAILS, e);
        }
    }

    @Override
    public void delete(String fileName) {
        log.info("Deleting file with name: {}", fileName);
        try {
            final Path filePath = storageService.createFilePath(fileName);
            storageService.deleteFile(filePath);
        } catch (Exception e) {
            // Need to prefix here because of my custom FileNotFoundException class
            if(e instanceof java.io.FileNotFoundException) {
                throw new FileNotFoundException(FILE_NOT_FOUND_ERROR, e);
            } else {
                throw new FileSystemException(DELETE_FILE_FAILS, e);
            }
        }
    }
}
