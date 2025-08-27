package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.exception.FileNameRequiredException;
import com.test_apps.motorola_coding_challenge.exception.FileNotFoundException;
import com.test_apps.motorola_coding_challenge.exception.FileSystemException;
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
import java.util.Optional;
import java.util.stream.Stream;

import static com.test_apps.motorola_coding_challenge.exception.ExceptionMessages.*;

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
            throw new FileSystemException(GET_ALL_FILES_FAILS, e);
        }
    }

    @Override
    public Resource get(String fileName) throws Exception {
        Optional.ofNullable(fileName)
                .filter(name -> !name.isEmpty())
                .orElseThrow(() -> new FileNameRequiredException(FILE_NAME_REQUIRED_ERROR));
        log.info("Getting file with name: {}", fileName);

        final Path filePath = storageService.createFilePath(fileName);
        final URI filePathUri = storageService.createUri(filePath);
        final Resource resource = storageService.createResource(filePathUri);

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException(FILE_NOT_FOUND_ERROR);
        }
        return resource;
    }

    @Override
    public String save(MultipartFile file) throws Exception {
        Optional.of(file)
                .filter(f -> !f.getName().isEmpty())
                .orElseThrow(() -> new FileNameRequiredException(FILE_NAME_REQUIRED_ERROR));

        final String fileName = file.getName();

        log.info("Saving file with name: {}", fileName);

        final Path filePath = storageService.createFilePath(fileName);

        final InputStream inputStream = file.getInputStream();

        storageService.copyFile(inputStream, filePath);

        return fileName;
    }

    @Override
    public void delete(String fileName) throws Exception {
        Optional.ofNullable(fileName)
                .filter(name -> !name.isEmpty())
                .orElseThrow(() -> new FileNameRequiredException(FILE_NAME_REQUIRED_ERROR));
        log.info("Deleting file with name: {}", fileName);

        final Path filePath = storageService.createFilePath(fileName);
        storageService.deleteFile(filePath);
    }
}
