package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.model.FileListDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileListDto listFiles();

    Resource getFile(String fileName);

    String postFile(MultipartFile file);

    void deleteFile(String fileName);
}
