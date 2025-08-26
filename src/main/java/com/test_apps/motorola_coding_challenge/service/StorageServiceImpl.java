package com.test_apps.motorola_coding_challenge.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Getter
public class StorageServiceImpl implements StorageService {
    private final String pathFromRoot = "src/main/java/com/test_apps/motorola_coding_challenge/repository/store/";

    @Override
    public Path createFilePath(String fileName) {
        String targetFileLocation = new StringBuilder(pathFromRoot)
                .append(fileName)
                .toString();
        return Path.of(targetFileLocation);
    }

    @Override
    public URI createUri(Path filePath) {
        return filePath.toUri();
    }

    @Override
    public Resource createResource(URI uri) throws Exception {
        return new UrlResource(uri);
    }
}
