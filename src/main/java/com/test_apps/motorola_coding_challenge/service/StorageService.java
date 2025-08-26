package com.test_apps.motorola_coding_challenge.service;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    Path getPathFromRoot();

    Stream<Path> getAllPaths(Path rootPath) throws Exception;

    Path createFilePath(String fileName);

    URI createUri(Path filePath);

    Resource createResource(URI uri) throws Exception;

    void copyFile(InputStream inputStream, Path filePath) throws Exception;

    void deleteFile(Path filePath) throws Exception;
}
