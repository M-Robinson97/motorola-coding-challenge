package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.service.util.StorageProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

/**
Service handling memory interactions. Separated out from repository to improve modularity and ease of testing.
 */
@Service
@RequiredArgsConstructor
@Getter
public class StorageServiceImpl implements StorageService {

    private final StorageProperties storageProperties;

    @Override
    public Path getPathFromRoot() {
        return storageProperties.getRootPath();
    }

    @Override
    public List<String> getAllPaths(Path rootPath) throws Exception {
        try (Stream<Path> stream = Files.walk(rootPath)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(rootPath::relativize)
                    .map(Path::toString)
                    .toList();
        }
    }

    @Override
    public Path createFilePath(String fileName) {
        return getPathFromRoot().resolve(fileName);
    }

    @Override
    public URI createUri(Path filePath) {
        return filePath.toUri();
    }

    @Override
    public Resource createResource(URI uri) throws Exception {
        return new UrlResource(uri);
    }

    @Override
    public void copyFile(InputStream inputStream, Path filePath) throws Exception {
        Files.createDirectories(filePath.getParent());
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void deleteFile(Path filePath) throws Exception {
        if(Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException();
        }
    }
}
