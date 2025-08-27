package com.test_apps.motorola_coding_challenge.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> listFiles() throws Exception;

    Resource getFile(String fileName) throws Exception;

    String postFile(MultipartFile file) throws Exception;

    void deleteFile(String fileName) throws Exception;
}
