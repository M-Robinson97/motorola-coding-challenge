package com.test_apps.motorola_coding_challenge.service;

import org.springframework.core.io.Resource;

import java.net.URI;
import java.nio.file.Path;

public interface StorageService {
    String getPathFromRoot();

    Path createFilePath(String fileName);

    URI createUri(Path filePath);

    Resource createResource(URI uri) throws Exception;
}
