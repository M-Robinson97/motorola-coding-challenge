package com.test_apps.motorola_coding_challenge.service;

import java.net.URI;
import java.nio.file.Path;

public interface StorageService {
    String getPathFromRoot();

    Path buildFilePath(String fileName);

    URI buildFilePathUri(String fileName);
}
