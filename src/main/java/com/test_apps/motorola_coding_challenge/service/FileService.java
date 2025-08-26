package com.test_apps.motorola_coding_challenge.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> listFiles();

    Resource getFile(String fileName);

    String postFile(MultipartFile file);

    String deleteFile(String fileName);
}
